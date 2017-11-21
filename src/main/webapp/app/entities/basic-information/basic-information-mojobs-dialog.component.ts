import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BasicInformationMojobs } from './basic-information-mojobs.model';
import { BasicInformationMojobsPopupService } from './basic-information-mojobs-popup.service';
import { BasicInformationMojobsService } from './basic-information-mojobs.service';
import { ResumeMojobs, ResumeMojobsService } from '../resume';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-basic-information-mojobs-dialog',
    templateUrl: './basic-information-mojobs-dialog.component.html'
})
export class BasicInformationMojobsDialogComponent implements OnInit {

    basicInformation: BasicInformationMojobs;
    isSaving: boolean;

    resumes: ResumeMojobs[];
    dateofBrithDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private basicInformationService: BasicInformationMojobsService,
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
        if (this.basicInformation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.basicInformationService.update(this.basicInformation));
        } else {
            this.subscribeToSaveResponse(
                this.basicInformationService.create(this.basicInformation));
        }
    }

    private subscribeToSaveResponse(result: Observable<BasicInformationMojobs>) {
        result.subscribe((res: BasicInformationMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: BasicInformationMojobs) {
        this.eventManager.broadcast({ name: 'basicInformationListModification', content: 'OK'});
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
    selector: 'jhi-basic-information-mojobs-popup',
    template: ''
})
export class BasicInformationMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private basicInformationPopupService: BasicInformationMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.basicInformationPopupService
                    .open(BasicInformationMojobsDialogComponent as Component, params['id']);
            } else {
                this.basicInformationPopupService
                    .open(BasicInformationMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
