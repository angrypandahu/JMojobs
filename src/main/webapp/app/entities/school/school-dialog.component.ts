import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { School } from './school.model';
import { SchoolPopupService } from './school-popup.service';
import { SchoolService } from './school.service';
import { Address, AddressService } from '../address';
import { Image, ImageService } from '../image';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-school-dialog',
    templateUrl: './school-dialog.component.html'
})
export class SchoolDialogComponent implements OnInit {

    school: School;
    isSaving: boolean;

    addresses: Address[];

    images: Image[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private schoolService: SchoolService,
        private addressService: AddressService,
        private imageService: ImageService,
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
                        .subscribe((subRes: Address) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.imageService
            .query({filter: 'school-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.school.imageId) {
                    this.images = res.json;
                } else {
                    this.imageService
                        .find(this.school.imageId)
                        .subscribe((subRes: Image) => {
                            this.images = [subRes].concat(res.json);
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

    private subscribeToSaveResponse(result: Observable<School>) {
        result.subscribe((res: School) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: School) {
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

    trackAddressById(index: number, item: Address) {
        return item.id;
    }

    trackImageById(index: number, item: Image) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-school-popup',
    template: ''
})
export class SchoolPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private schoolPopupService: SchoolPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.schoolPopupService
                    .open(SchoolDialogComponent as Component, params['id']);
            } else {
                this.schoolPopupService
                    .open(SchoolDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
