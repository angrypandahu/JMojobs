import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EducationBackgroundMojobs } from './education-background-mojobs.model';
import { EducationBackgroundMojobsPopupService } from './education-background-mojobs-popup.service';
import { EducationBackgroundMojobsService } from './education-background-mojobs.service';
import { ResumeMojobs, ResumeMojobsService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-education-background-mojobs-dialog',
    templateUrl: './education-background-mojobs-dialog.component.html'
})
export class EducationBackgroundMojobsDialogComponent implements OnInit {

    educationBackground: EducationBackgroundMojobs;
    isSaving: boolean;

    resumes: ResumeMojobs[];
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private educationBackgroundService: EducationBackgroundMojobsService,
        private resumeService: ResumeMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.resumeService.query()
            .subscribe((res: ResponseWrapper) => { this.resumes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.educationBackground.id !== undefined) {
            this.subscribeToSaveResponse(
                this.educationBackgroundService.update(this.educationBackground));
        } else {
            this.subscribeToSaveResponse(
                this.educationBackgroundService.create(this.educationBackground));
        }
    }

    private subscribeToSaveResponse(result: Observable<EducationBackgroundMojobs>) {
        result.subscribe((res: EducationBackgroundMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: EducationBackgroundMojobs) {
        this.eventManager.broadcast({ name: 'educationBackgroundListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-education-background-mojobs-popup',
    template: ''
})
export class EducationBackgroundMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private educationBackgroundPopupService: EducationBackgroundMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.educationBackgroundPopupService
                    .open(EducationBackgroundMojobsDialogComponent as Component, params['id']);
            } else {
                this.educationBackgroundPopupService
                    .open(EducationBackgroundMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
