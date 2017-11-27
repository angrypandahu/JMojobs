import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EducationBackground } from './education-background.model';
import { EducationBackgroundPopupService } from './education-background-popup.service';
import { EducationBackgroundService } from './education-background.service';
import { Resume, ResumeService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-education-background-dialog',
    templateUrl: './education-background-dialog.component.html'
})
export class EducationBackgroundDialogComponent implements OnInit {

    educationBackground: EducationBackground;
    isSaving: boolean;

    resumes: Resume[];
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private educationBackgroundService: EducationBackgroundService,
        private resumeService: ResumeService,
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

    private subscribeToSaveResponse(result: Observable<EducationBackground>) {
        result.subscribe((res: EducationBackground) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: EducationBackground) {
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

    trackResumeById(index: number, item: Resume) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-education-background-popup',
    template: ''
})
export class EducationBackgroundPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private educationBackgroundPopupService: EducationBackgroundPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.educationBackgroundPopupService
                    .open(EducationBackgroundDialogComponent as Component, params['id']);
            } else {
                this.educationBackgroundPopupService
                    .open(EducationBackgroundDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
