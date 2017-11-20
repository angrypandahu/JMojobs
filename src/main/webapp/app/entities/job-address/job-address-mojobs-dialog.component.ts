import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobAddressMojobs } from './job-address-mojobs.model';
import { JobAddressMojobsPopupService } from './job-address-mojobs-popup.service';
import { JobAddressMojobsService } from './job-address-mojobs.service';
import { AddressMojobs, AddressMojobsService } from '../address';
import { MjobMojobs, MjobMojobsService } from '../mjob';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-job-address-mojobs-dialog',
    templateUrl: './job-address-mojobs-dialog.component.html'
})
export class JobAddressMojobsDialogComponent implements OnInit {

    jobAddress: JobAddressMojobs;
    isSaving: boolean;

    addresses: AddressMojobs[];

    mjobs: MjobMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobAddressService: JobAddressMojobsService,
        private addressService: AddressMojobsService,
        private mjobService: MjobMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.addressService
            .query({filter: 'jobaddress-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.jobAddress.addressId) {
                    this.addresses = res.json;
                } else {
                    this.addressService
                        .find(this.jobAddress.addressId)
                        .subscribe((subRes: AddressMojobs) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.mjobService.query()
            .subscribe((res: ResponseWrapper) => { this.mjobs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.jobAddress.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobAddressService.update(this.jobAddress));
        } else {
            this.subscribeToSaveResponse(
                this.jobAddressService.create(this.jobAddress));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobAddressMojobs>) {
        result.subscribe((res: JobAddressMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobAddressMojobs) {
        this.eventManager.broadcast({ name: 'jobAddressListModification', content: 'OK'});
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

    trackMjobById(index: number, item: MjobMojobs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-job-address-mojobs-popup',
    template: ''
})
export class JobAddressMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobAddressPopupService: JobAddressMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobAddressPopupService
                    .open(JobAddressMojobsDialogComponent as Component, params['id']);
            } else {
                this.jobAddressPopupService
                    .open(JobAddressMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
