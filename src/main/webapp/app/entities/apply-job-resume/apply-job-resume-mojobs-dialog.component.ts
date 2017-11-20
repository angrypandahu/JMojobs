import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ApplyJobResumeMojobs } from './apply-job-resume-mojobs.model';
import { ApplyJobResumeMojobsPopupService } from './apply-job-resume-mojobs-popup.service';
import { ApplyJobResumeMojobsService } from './apply-job-resume-mojobs.service';
import { ResumeMojobs, ResumeMojobsService } from '../resume';
import { MjobMojobs, MjobMojobsService } from '../mjob';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-apply-job-resume-mojobs-dialog',
    templateUrl: './apply-job-resume-mojobs-dialog.component.html'
})
export class ApplyJobResumeMojobsDialogComponent implements OnInit {

    applyJobResume: ApplyJobResumeMojobs;
    isSaving: boolean;

    resumes: ResumeMojobs[];

    mjobs: MjobMojobs[];
    applyDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private applyJobResumeService: ApplyJobResumeMojobsService,
        private resumeService: ResumeMojobsService,
        private mjobService: MjobMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.resumeService.query()
            .subscribe((res: ResponseWrapper) => { this.resumes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.mjobService.query()
            .subscribe((res: ResponseWrapper) => { this.mjobs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
        if (this.applyJobResume.id !== undefined) {
            this.subscribeToSaveResponse(
                this.applyJobResumeService.update(this.applyJobResume));
        } else {
            this.subscribeToSaveResponse(
                this.applyJobResumeService.create(this.applyJobResume));
        }
    }

    private subscribeToSaveResponse(result: Observable<ApplyJobResumeMojobs>) {
        result.subscribe((res: ApplyJobResumeMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ApplyJobResumeMojobs) {
        this.eventManager.broadcast({ name: 'applyJobResumeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackResumeById(index: number, item: ResumeMojobs) {
        return item.id;
    }

    trackMjobById(index: number, item: MjobMojobs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-apply-job-resume-mojobs-popup',
    template: ''
})
export class ApplyJobResumeMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applyJobResumePopupService: ApplyJobResumeMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.applyJobResumePopupService
                    .open(ApplyJobResumeMojobsDialogComponent as Component, params['id']);
            } else {
                this.applyJobResumePopupService
                    .open(ApplyJobResumeMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
