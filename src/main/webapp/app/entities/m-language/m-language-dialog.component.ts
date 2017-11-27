import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MLanguage } from './m-language.model';
import { MLanguagePopupService } from './m-language-popup.service';
import { MLanguageService } from './m-language.service';
import { Resume, ResumeService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-m-language-dialog',
    templateUrl: './m-language-dialog.component.html'
})
export class MLanguageDialogComponent implements OnInit {

    mLanguage: MLanguage;
    isSaving: boolean;

    resumes: Resume[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private mLanguageService: MLanguageService,
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
        if (this.mLanguage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mLanguageService.update(this.mLanguage));
        } else {
            this.subscribeToSaveResponse(
                this.mLanguageService.create(this.mLanguage));
        }
    }

    private subscribeToSaveResponse(result: Observable<MLanguage>) {
        result.subscribe((res: MLanguage) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MLanguage) {
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

    trackResumeById(index: number, item: Resume) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-m-language-popup',
    template: ''
})
export class MLanguagePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mLanguagePopupService: MLanguagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mLanguagePopupService
                    .open(MLanguageDialogComponent as Component, params['id']);
            } else {
                this.mLanguagePopupService
                    .open(MLanguageDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
