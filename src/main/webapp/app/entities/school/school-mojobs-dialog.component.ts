import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SchoolMojobs } from './school-mojobs.model';
import { SchoolMojobsPopupService } from './school-mojobs-popup.service';
import { SchoolMojobsService } from './school-mojobs.service';
import { MojobImageMojobs, MojobImageMojobsService } from '../mojob-image';
import { AddressMojobs, AddressMojobsService } from '../address';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-school-mojobs-dialog',
    templateUrl: './school-mojobs-dialog.component.html'
})
export class SchoolMojobsDialogComponent implements OnInit {

    school: SchoolMojobs;
    isSaving: boolean;

    logos: MojobImageMojobs[];

    addresses: AddressMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private schoolService: SchoolMojobsService,
        private mojobImageService: MojobImageMojobsService,
        private addressService: AddressMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.mojobImageService
            .query({filter: 'school-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.school.logoId) {
                    this.logos = res.json;
                } else {
                    this.mojobImageService
                        .find(this.school.logoId)
                        .subscribe((subRes: MojobImageMojobs) => {
                            this.logos = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.addressService
            .query({filter: 'school-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.school.addressId) {
                    this.addresses = res.json;
                } else {
                    this.addressService
                        .find(this.school.addressId)
                        .subscribe((subRes: AddressMojobs) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.school.id !== undefined) {
            this.subscribeToSaveResponse(
                this.schoolService.update(this.school));
        } else {
            this.subscribeToSaveResponse(
                this.schoolService.create(this.school));
        }
    }

    private subscribeToSaveResponse(result: Observable<SchoolMojobs>) {
        result.subscribe((res: SchoolMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SchoolMojobs) {
        this.eventManager.broadcast({ name: 'schoolListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackMojobImageById(index: number, item: MojobImageMojobs) {
        return item.id;
    }

    trackAddressById(index: number, item: AddressMojobs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-school-mojobs-popup',
    template: ''
})
export class SchoolMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private schoolPopupService: SchoolMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.schoolPopupService
                    .open(SchoolMojobsDialogComponent as Component, params['id']);
            } else {
                this.schoolPopupService
                    .open(SchoolMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
