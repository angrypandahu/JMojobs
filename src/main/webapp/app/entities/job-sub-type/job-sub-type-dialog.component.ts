import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobSubType } from './job-sub-type.model';
import { JobSubTypePopupService } from './job-sub-type-popup.service';
import { JobSubTypeService } from './job-sub-type.service';

@Component({
    selector: 'jhi-job-sub-type-dialog',
    templateUrl: './job-sub-type-dialog.component.html'
})
export class JobSubTypeDialogComponent implements OnInit {

    jobSubType: JobSubType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobSubTypeService: JobSubTypeService,
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
        if (this.jobSubType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobSubTypeService.update(this.jobSubType));
        } else {
            this.subscribeToSaveResponse(
                this.jobSubTypeService.create(this.jobSubType));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobSubType>) {
        result.subscribe((res: JobSubType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobSubType) {
        this.eventManager.broadcast({ name: 'jobSubTypeListModification', content: 'OK'});
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
    selector: 'jhi-job-sub-type-popup',
    template: ''
})
export class JobSubTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobSubTypePopupService: JobSubTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobSubTypePopupService
                    .open(JobSubTypeDialogComponent as Component, params['id']);
            } else {
                this.jobSubTypePopupService
                    .open(JobSubTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
