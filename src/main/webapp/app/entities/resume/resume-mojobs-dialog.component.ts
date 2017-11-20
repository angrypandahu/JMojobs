import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ResumeMojobs } from './resume-mojobs.model';
import { ResumeMojobsPopupService } from './resume-mojobs-popup.service';
import { ResumeMojobsService } from './resume-mojobs.service';
import { BasicInformationMojobs, BasicInformationMojobsService } from '../basic-information';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-resume-mojobs-dialog',
    templateUrl: './resume-mojobs-dialog.component.html'
})
export class ResumeMojobsDialogComponent implements OnInit {

    resume: ResumeMojobs;
    isSaving: boolean;

    basicinformations: BasicInformationMojobs[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private resumeService: ResumeMojobsService,
        private basicInformationService: BasicInformationMojobsService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.basicInformationService
            .query({filter: 'resume(name)-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.resume.basicInformationId) {
                    this.basicinformations = res.json;
                } else {
                    this.basicInformationService
                        .find(this.resume.basicInformationId)
                        .subscribe((subRes: BasicInformationMojobs) => {
                            this.basicinformations = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.resume.id !== undefined) {
            this.subscribeToSaveResponse(
                this.resumeService.update(this.resume));
        } else {
            this.subscribeToSaveResponse(
                this.resumeService.create(this.resume));
        }
    }

    private subscribeToSaveResponse(result: Observable<ResumeMojobs>) {
        result.subscribe((res: ResumeMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ResumeMojobs) {
        this.eventManager.broadcast({ name: 'resumeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBasicInformationById(index: number, item: BasicInformationMojobs) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-resume-mojobs-popup',
    template: ''
})
export class ResumeMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private resumePopupService: ResumeMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.resumePopupService
                    .open(ResumeMojobsDialogComponent as Component, params['id']);
            } else {
                this.resumePopupService
                    .open(ResumeMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
