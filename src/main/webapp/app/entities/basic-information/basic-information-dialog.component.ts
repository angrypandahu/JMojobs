import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BasicInformation } from './basic-information.model';
import { BasicInformationPopupService } from './basic-information-popup.service';
import { BasicInformationService } from './basic-information.service';
import { Resume, ResumeService } from '../resume';
import { Image, ImageService } from '../image';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-basic-information-dialog',
    templateUrl: './basic-information-dialog.component.html'
})
export class BasicInformationDialogComponent implements OnInit {

    basicInformation: BasicInformation;
    isSaving: boolean;

    resumes: Resume[];

    images: Image[];
    dateofBrithDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private basicInformationService: BasicInformationService,
        private resumeService: ResumeService,
        private imageService: ImageService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.resumeService.query()
            .subscribe((res: ResponseWrapper) => { this.resumes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.imageService
            .query({filter: 'basicinformation-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.basicInformation.imageId) {
                    this.images = res.json;
                } else {
                    this.imageService
                        .find(this.basicInformation.imageId)
                        .subscribe((subRes: Image) => {
                            this.images = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
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

    private subscribeToSaveResponse(result: Observable<BasicInformation>) {
        result.subscribe((res: BasicInformation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: BasicInformation) {
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

    trackResumeById(index: number, item: Resume) {
        return item.id;
    }

    trackImageById(index: number, item: Image) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-basic-information-popup',
    template: ''
})
export class BasicInformationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private basicInformationPopupService: BasicInformationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.basicInformationPopupService
                    .open(BasicInformationDialogComponent as Component, params['id']);
            } else {
                this.basicInformationPopupService
                    .open(BasicInformationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
