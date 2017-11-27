import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ApplyJobResume } from './apply-job-resume.model';
import { ApplyJobResumePopupService } from './apply-job-resume-popup.service';
import { ApplyJobResumeService } from './apply-job-resume.service';
import { Resume, ResumeService } from '../resume';
import { Mjob, MjobService } from '../mjob';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-apply-job-resume-dialog',
    templateUrl: './apply-job-resume-dialog.component.html'
})
export class ApplyJobResumeDialogComponent implements OnInit {

    applyJobResume: ApplyJobResume;
    isSaving: boolean;

    resumes: Resume[];

    mjobs: Mjob[];
    applyDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private applyJobResumeService: ApplyJobResumeService,
        private resumeService: ResumeService,
        private mjobService: MjobService,
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

    private subscribeToSaveResponse(result: Observable<ApplyJobResume>) {
        result.subscribe((res: ApplyJobResume) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ApplyJobResume) {
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

    trackResumeById(index: number, item: Resume) {
        return item.id;
    }

    trackMjobById(index: number, item: Mjob) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-apply-job-resume-popup',
    template: ''
})
export class ApplyJobResumePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applyJobResumePopupService: ApplyJobResumePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.applyJobResumePopupService
                    .open(ApplyJobResumeDialogComponent as Component, params['id']);
            } else {
                this.applyJobResumePopupService
                    .open(ApplyJobResumeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
