import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProfessionalDevelopmentMojobs } from './professional-development-mojobs.model';
import { ProfessionalDevelopmentMojobsPopupService } from './professional-development-mojobs-popup.service';
import { ProfessionalDevelopmentMojobsService } from './professional-development-mojobs.service';
import { ResumeMojobs, ResumeMojobsService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-professional-development-mojobs-dialog',
    templateUrl: './professional-development-mojobs-dialog.component.html'
})
export class ProfessionalDevelopmentMojobsDialogComponent implements OnInit {

    professionalDevelopment: ProfessionalDevelopmentMojobs;
    isSaving: boolean;

    resumes: ResumeMojobs[];
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private professionalDevelopmentService: ProfessionalDevelopmentMojobsService,
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
        if (this.professionalDevelopment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.professionalDevelopmentService.update(this.professionalDevelopment));
        } else {
            this.subscribeToSaveResponse(
                this.professionalDevelopmentService.create(this.professionalDevelopment));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProfessionalDevelopmentMojobs>) {
        result.subscribe((res: ProfessionalDevelopmentMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProfessionalDevelopmentMojobs) {
        this.eventManager.broadcast({ name: 'professionalDevelopmentListModification', content: 'OK'});
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
    selector: 'jhi-professional-development-mojobs-popup',
    template: ''
})
export class ProfessionalDevelopmentMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private professionalDevelopmentPopupService: ProfessionalDevelopmentMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.professionalDevelopmentPopupService
                    .open(ProfessionalDevelopmentMojobsDialogComponent as Component, params['id']);
            } else {
                this.professionalDevelopmentPopupService
                    .open(ProfessionalDevelopmentMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
