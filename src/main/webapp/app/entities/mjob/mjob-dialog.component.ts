import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Mjob } from './mjob.model';
import { MjobPopupService } from './mjob-popup.service';
import { MjobService } from './mjob.service';
import { Address, AddressService } from '../address';
import { School, SchoolService } from '../school';
import { JobSubType, JobSubTypeService } from '../job-sub-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-mjob-dialog',
    templateUrl: './mjob-dialog.component.html'
})
export class MjobDialogComponent implements OnInit {

    mjob: Mjob;
    isSaving: boolean;

    addresses: Address[];

    schools: School[];

    jobsubtypes: JobSubType[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private mjobService: MjobService,
        private addressService: AddressService,
        private schoolService: SchoolService,
        private jobSubTypeService: JobSubTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.addressService
            .query({filter: 'mjob-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.mjob.addressId) {
                    this.addresses = res.json;
                } else {
                    this.addressService
                        .find(this.mjob.addressId)
                        .subscribe((subRes: Address) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.schoolService.query()
            .subscribe((res: ResponseWrapper) => { this.schools = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.jobSubTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.jobsubtypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.mjob.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mjobService.update(this.mjob));
        } else {
            this.subscribeToSaveResponse(
                this.mjobService.create(this.mjob));
        }
    }

    private subscribeToSaveResponse(result: Observable<Mjob>) {
        result.subscribe((res: Mjob) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Mjob) {
        this.eventManager.broadcast({ name: 'mjobListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAddressById(index: number, item: Address) {
        return item.id;
    }

    trackSchoolById(index: number, item: School) {
        return item.id;
    }

    trackJobSubTypeById(index: number, item: JobSubType) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-mjob-popup',
    template: ''
})
export class MjobPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mjobPopupService: MjobPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mjobPopupService
                    .open(MjobDialogComponent as Component, params['id']);
            } else {
                this.mjobPopupService
                    .open(MjobDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
