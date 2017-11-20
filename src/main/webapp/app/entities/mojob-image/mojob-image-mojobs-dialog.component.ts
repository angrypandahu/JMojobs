import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { MojobImageMojobs } from './mojob-image-mojobs.model';
import { MojobImageMojobsPopupService } from './mojob-image-mojobs-popup.service';
import { MojobImageMojobsService } from './mojob-image-mojobs.service';

@Component({
    selector: 'jhi-mojob-image-mojobs-dialog',
    templateUrl: './mojob-image-mojobs-dialog.component.html'
})
export class MojobImageMojobsDialogComponent implements OnInit {

    mojobImage: MojobImageMojobs;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private mojobImageService: MojobImageMojobsService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.mojobImage, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.mojobImage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mojobImageService.update(this.mojobImage));
        } else {
            this.subscribeToSaveResponse(
                this.mojobImageService.create(this.mojobImage));
        }
    }

    private subscribeToSaveResponse(result: Observable<MojobImageMojobs>) {
        result.subscribe((res: MojobImageMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MojobImageMojobs) {
        this.eventManager.broadcast({ name: 'mojobImageListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-mojob-image-mojobs-popup',
    template: ''
})
export class MojobImageMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mojobImagePopupService: MojobImageMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mojobImagePopupService
                    .open(MojobImageMojobsDialogComponent as Component, params['id']);
            } else {
                this.mojobImagePopupService
                    .open(MojobImageMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
