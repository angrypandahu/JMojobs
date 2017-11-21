import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { SchoolMojobs } from './school-mojobs.model';
import { SchoolMojobsPopupService } from './school-mojobs-popup.service';
import { SchoolMojobsService } from './school-mojobs.service';
import { AddressMojobs, AddressMojobsService } from '../address';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-school-mojobs-dialog',
    templateUrl: './school-mojobs-dialog.component.html'
})
export class SchoolMojobsDialogComponent implements OnInit {

    school: SchoolMojobs;
    isSaving: boolean;

    addresses: AddressMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private schoolService: SchoolMojobsService,
        private addressService: AddressMojobsService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.school, this.elementRef, field, fieldContentType, idInput);
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
