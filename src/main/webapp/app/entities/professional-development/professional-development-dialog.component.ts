import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProfessionalDevelopment } from './professional-development.model';
import { ProfessionalDevelopmentPopupService } from './professional-development-popup.service';
import { ProfessionalDevelopmentService } from './professional-development.service';
import { Resume, ResumeService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-professional-development-dialog',
    templateUrl: './professional-development-dialog.component.html'
})
export class ProfessionalDevelopmentDialogComponent implements OnInit {

    professionalDevelopment: ProfessionalDevelopment;
    isSaving: boolean;

    resumes: Resume[];
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private professionalDevelopmentService: ProfessionalDevelopmentService,
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
        if (this.professionalDevelopment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.professionalDevelopmentService.update(this.professionalDevelopment));
        } else {
            this.subscribeToSaveResponse(
                this.professionalDevelopmentService.create(this.professionalDevelopment));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProfessionalDevelopment>) {
        result.subscribe((res: ProfessionalDevelopment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProfessionalDevelopment) {
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

    trackResumeById(index: number, item: Resume) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-professional-development-popup',
    template: ''
})
export class ProfessionalDevelopmentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private professionalDevelopmentPopupService: ProfessionalDevelopmentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.professionalDevelopmentPopupService
                    .open(ProfessionalDevelopmentDialogComponent as Component, params['id']);
            } else {
                this.professionalDevelopmentPopupService
                    .open(ProfessionalDevelopmentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
