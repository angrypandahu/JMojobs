import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CityMojobs } from './city-mojobs.model';
import { CityMojobsPopupService } from './city-mojobs-popup.service';
import { CityMojobsService } from './city-mojobs.service';
import { ProvinceMojobs, ProvinceMojobsService } from '../province';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-city-mojobs-dialog',
    templateUrl: './city-mojobs-dialog.component.html'
})
export class CityMojobsDialogComponent implements OnInit {

    city: CityMojobs;
    isSaving: boolean;

    provinces: ProvinceMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cityService: CityMojobsService,
        private provinceService: ProvinceMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.provinceService.query()
            .subscribe((res: ResponseWrapper) => { this.provinces = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.city.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cityService.update(this.city));
        } else {
            this.subscribeToSaveResponse(
                this.cityService.create(this.city));
        }
    }

    private subscribeToSaveResponse(result: Observable<CityMojobs>) {
        result.subscribe((res: CityMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CityMojobs) {
        this.eventManager.broadcast({ name: 'cityListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-city-mojobs-popup',
    template: ''
})
export class CityMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cityPopupService: CityMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cityPopupService
                    .open(CityMojobsDialogComponent as Component, params['id']);
            } else {
                this.cityPopupService
                    .open(CityMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
