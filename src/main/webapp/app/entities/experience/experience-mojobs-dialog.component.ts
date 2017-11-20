import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ExperienceMojobs } from './experience-mojobs.model';
import { ExperienceMojobsPopupService } from './experience-mojobs-popup.service';
import { ExperienceMojobsService } from './experience-mojobs.service';
import { ResumeMojobs, ResumeMojobsService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-experience-mojobs-dialog',
    templateUrl: './experience-mojobs-dialog.component.html'
})
export class ExperienceMojobsDialogComponent implements OnInit {

    experience: ExperienceMojobs;
    isSaving: boolean;

    resumes: ResumeMojobs[];
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private experienceService: ExperienceMojobsService,
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
        if (this.experience.id !== undefined) {
            this.subscribeToSaveResponse(
                this.experienceService.update(this.experience));
        } else {
            this.subscribeToSaveResponse(
                this.experienceService.create(this.experience));
        }
    }

    private subscribeToSaveResponse(result: Observable<ExperienceMojobs>) {
        result.subscribe((res: ExperienceMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ExperienceMojobs) {
        this.eventManager.broadcast({ name: 'experienceListModification', content: 'OK'});
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
    selector: 'jhi-experience-mojobs-popup',
    template: ''
})
export class ExperienceMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private experiencePopupService: ExperienceMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.experiencePopupService
                    .open(ExperienceMojobsDialogComponent as Component, params['id']);
            } else {
                this.experiencePopupService
                    .open(ExperienceMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
