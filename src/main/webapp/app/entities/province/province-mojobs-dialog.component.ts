import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProvinceMojobs } from './province-mojobs.model';
import { ProvinceMojobsPopupService } from './province-mojobs-popup.service';
import { ProvinceMojobsService } from './province-mojobs.service';

@Component({
    selector: 'jhi-province-mojobs-dialog',
    templateUrl: './province-mojobs-dialog.component.html'
})
export class ProvinceMojobsDialogComponent implements OnInit {

    province: ProvinceMojobs;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private provinceService: ProvinceMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.province.id !== undefined) {
            this.subscribeToSaveResponse(
                this.provinceService.update(this.province));
        } else {
            this.subscribeToSaveResponse(
                this.provinceService.create(this.province));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProvinceMojobs>) {
        result.subscribe((res: ProvinceMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProvinceMojobs) {
        this.eventManager.broadcast({ name: 'provinceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-province-mojobs-popup',
    template: ''
})
export class ProvinceMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private provincePopupService: ProvinceMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.provincePopupService
                    .open(ProvinceMojobsDialogComponent as Component, params['id']);
            } else {
                this.provincePopupService
                    .open(ProvinceMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
