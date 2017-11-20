import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobSubTypeMojobs } from './job-sub-type-mojobs.model';
import { JobSubTypeMojobsPopupService } from './job-sub-type-mojobs-popup.service';
import { JobSubTypeMojobsService } from './job-sub-type-mojobs.service';

@Component({
    selector: 'jhi-job-sub-type-mojobs-dialog',
    templateUrl: './job-sub-type-mojobs-dialog.component.html'
})
export class JobSubTypeMojobsDialogComponent implements OnInit {

    jobSubType: JobSubTypeMojobs;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobSubTypeService: JobSubTypeMojobsService,
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

    private subscribeToSaveResponse(result: Observable<JobSubTypeMojobs>) {
        result.subscribe((res: JobSubTypeMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobSubTypeMojobs) {
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
    selector: 'jhi-job-sub-type-mojobs-popup',
    template: ''
})
export class JobSubTypeMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobSubTypePopupService: JobSubTypeMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobSubTypePopupService
                    .open(JobSubTypeMojobsDialogComponent as Component, params['id']);
            } else {
                this.jobSubTypePopupService
                    .open(JobSubTypeMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
