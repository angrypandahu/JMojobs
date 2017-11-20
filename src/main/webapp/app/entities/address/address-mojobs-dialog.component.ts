import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AddressMojobs } from './address-mojobs.model';
import { AddressMojobsPopupService } from './address-mojobs-popup.service';
import { AddressMojobsService } from './address-mojobs.service';
import { ProvinceMojobs, ProvinceMojobsService } from '../province';
import { CityMojobs, CityMojobsService } from '../city';
import { TownMojobs, TownMojobsService } from '../town';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-address-mojobs-dialog',
    templateUrl: './address-mojobs-dialog.component.html'
})
export class AddressMojobsDialogComponent implements OnInit {

    address: AddressMojobs;
    isSaving: boolean;

    provinces: ProvinceMojobs[];

    cities: CityMojobs[];

    towns: TownMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private addressService: AddressMojobsService,
        private provinceService: ProvinceMojobsService,
        private cityService: CityMojobsService,
        private townService: TownMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.provinceService.query()
            .subscribe((res: ResponseWrapper) => { this.provinces = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.cityService.query()
            .subscribe((res: ResponseWrapper) => { this.cities = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.townService.query()
            .subscribe((res: ResponseWrapper) => { this.towns = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.address.id !== undefined) {
            this.subscribeToSaveResponse(
                this.addressService.update(this.address));
        } else {
            this.subscribeToSaveResponse(
                this.addressService.create(this.address));
        }
    }

    private subscribeToSaveResponse(result: Observable<AddressMojobs>) {
        result.subscribe((res: AddressMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AddressMojobs) {
        this.eventManager.broadcast({ name: 'addressListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProvinceById(index: number, item: ProvinceMojobs) {
        return item.id;
    }

    trackCityById(index: number, item: CityMojobs) {
        return item.id;
    }

    trackTownById(index: number, item: TownMojobs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-address-mojobs-popup',
    template: ''
})
export class AddressMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private addressPopupService: AddressMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.addressPopupService
                    .open(AddressMojobsDialogComponent as Component, params['id']);
            } else {
                this.addressPopupService
                    .open(AddressMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
