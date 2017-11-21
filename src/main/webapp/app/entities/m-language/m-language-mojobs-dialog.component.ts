import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MLanguageMojobs } from './m-language-mojobs.model';
import { MLanguageMojobsPopupService } from './m-language-mojobs-popup.service';
import { MLanguageMojobsService } from './m-language-mojobs.service';
import { ResumeMojobs, ResumeMojobsService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-m-language-mojobs-dialog',
    templateUrl: './m-language-mojobs-dialog.component.html'
})
export class MLanguageMojobsDialogComponent implements OnInit {

    mLanguage: MLanguageMojobs;
    isSaving: boolean;

    resumes: ResumeMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private mLanguageService: MLanguageMojobsService,
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
        if (this.mLanguage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mLanguageService.update(this.mLanguage));
        } else {
            this.subscribeToSaveResponse(
                this.mLanguageService.create(this.mLanguage));
        }
    }

    private subscribeToSaveResponse(result: Observable<MLanguageMojobs>) {
        result.subscribe((res: MLanguageMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MLanguageMojobs) {
        this.eventManager.broadcast({ name: 'mLanguageListModification', content: 'OK'});
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
    selector: 'jhi-m-language-mojobs-popup',
    template: ''
})
export class MLanguageMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mLanguagePopupService: MLanguageMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mLanguagePopupService
                    .open(MLanguageMojobsDialogComponent as Component, params['id']);
            } else {
                this.mLanguagePopupService
                    .open(MLanguageMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
