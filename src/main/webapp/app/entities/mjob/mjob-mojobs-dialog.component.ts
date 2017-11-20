import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { MjobMojobs } from './mjob-mojobs.model';
import { MjobMojobsPopupService } from './mjob-mojobs-popup.service';
import { MjobMojobsService } from './mjob-mojobs.service';
import { SchoolMojobs, SchoolMojobsService } from '../school';
import { JobSubTypeMojobs, JobSubTypeMojobsService } from '../job-sub-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-mjob-mojobs-dialog',
    templateUrl: './mjob-mojobs-dialog.component.html'
})
export class MjobMojobsDialogComponent implements OnInit {

    mjob: MjobMojobs;
    isSaving: boolean;

    schools: SchoolMojobs[];

    jobsubtypes: JobSubTypeMojobs[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private mjobService: MjobMojobsService,
        private schoolService: SchoolMojobsService,
        private jobSubTypeService: JobSubTypeMojobsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.schoolService.query()
            .subscribe((res: ResponseWrapper) => { this.schools = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.jobSubTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.jobsubtypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.mjob.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mjobService.update(this.mjob));
        } else {
            this.subscribeToSaveResponse(
                this.mjobService.create(this.mjob));
        }
    }

    private subscribeToSaveResponse(result: Observable<MjobMojobs>) {
        result.subscribe((res: MjobMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MjobMojobs) {
        this.eventManager.broadcast({ name: 'mjobListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSchoolById(index: number, item: SchoolMojobs) {
        return item.id;
    }

    trackJobSubTypeById(index: number, item: JobSubTypeMojobs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-mjob-mojobs-popup',
    template: ''
})
export class MjobMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mjobPopupService: MjobMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mjobPopupService
                    .open(MjobMojobsDialogComponent as Component, params['id']);
            } else {
                this.mjobPopupService
                    .open(MjobMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
