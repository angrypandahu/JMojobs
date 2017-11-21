import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TownMojobs } from './town-mojobs.model';
import { TownMojobsPopupService } from './town-mojobs-popup.service';
import { TownMojobsService } from './town-mojobs.service';
import { CityMojobs, CityMojobsService } from '../city';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-town-mojobs-dialog',
    templateUrl: './town-mojobs-dialog.component.html'
})
export class TownMojobsDialogComponent implements OnInit {

    town: TownMojobs;
    isSaving: boolean;

    cities: CityMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private townService: TownMojobsService,
        private cityService: CityMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cityService.query()
            .subscribe((res: ResponseWrapper) => { this.cities = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.town.id !== undefined) {
            this.subscribeToSaveResponse(
                this.townService.update(this.town));
        } else {
            this.subscribeToSaveResponse(
                this.townService.create(this.town));
        }
    }

    private subscribeToSaveResponse(result: Observable<TownMojobs>) {
        result.subscribe((res: TownMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TownMojobs) {
        this.eventManager.broadcast({ name: 'townListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCityById(index: number, item: CityMojobs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-town-mojobs-popup',
    template: ''
})
export class TownMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private townPopupService: TownMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.townPopupService
                    .open(TownMojobsDialogComponent as Component, params['id']);
            } else {
                this.townPopupService
                    .open(TownMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
