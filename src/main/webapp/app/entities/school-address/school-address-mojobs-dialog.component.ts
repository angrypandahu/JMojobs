import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SchoolAddressMojobs } from './school-address-mojobs.model';
import { SchoolAddressMojobsPopupService } from './school-address-mojobs-popup.service';
import { SchoolAddressMojobsService } from './school-address-mojobs.service';
import { AddressMojobs, AddressMojobsService } from '../address';
import { SchoolMojobs, SchoolMojobsService } from '../school';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-school-address-mojobs-dialog',
    templateUrl: './school-address-mojobs-dialog.component.html'
})
export class SchoolAddressMojobsDialogComponent implements OnInit {

    schoolAddress: SchoolAddressMojobs;
    isSaving: boolean;

    addresses: AddressMojobs[];

    schools: SchoolMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private schoolAddressService: SchoolAddressMojobsService,
        private addressService: AddressMojobsService,
        private schoolService: SchoolMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.addressService
            .query({filter: 'schooladdress-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.schoolAddress.addressId) {
                    this.addresses = res.json;
                } else {
                    this.addressService
                        .find(this.schoolAddress.addressId)
                        .subscribe((subRes: AddressMojobs) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.schoolService.query()
            .subscribe((res: ResponseWrapper) => { this.schools = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.schoolAddress.id !== undefined) {
            this.subscribeToSaveResponse(
                this.schoolAddressService.update(this.schoolAddress));
        } else {
            this.subscribeToSaveResponse(
                this.schoolAddressService.create(this.schoolAddress));
        }
    }

    private subscribeToSaveResponse(result: Observable<SchoolAddressMojobs>) {
        result.subscribe((res: SchoolAddressMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SchoolAddressMojobs) {
        this.eventManager.broadcast({ name: 'schoolAddressListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAddressById(index: number, item: AddressMojobs) {
        return item.id;
    }

    trackSchoolById(index: number, item: SchoolMojobs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-school-address-mojobs-popup',
    template: ''
})
export class SchoolAddressMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private schoolAddressPopupService: SchoolAddressMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.schoolAddressPopupService
                    .open(SchoolAddressMojobsDialogComponent as Component, params['id']);
            } else {
                this.schoolAddressPopupService
                    .open(SchoolAddressMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
