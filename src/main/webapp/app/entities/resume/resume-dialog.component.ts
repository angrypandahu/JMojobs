import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Resume } from './resume.model';
import { ResumePopupService } from './resume-popup.service';
import { ResumeService } from './resume.service';
import { BasicInformation, BasicInformationService } from '../basic-information';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-resume-dialog',
    templateUrl: './resume-dialog.component.html'
})
export class ResumeDialogComponent implements OnInit {

    resume: Resume;
    isSaving: boolean;

    basicinformations: BasicInformation[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private resumeService: ResumeService,
        private basicInformationService: BasicInformationService,
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
                        .subscribe((subRes: BasicInformation) => {
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

    private subscribeToSaveResponse(result: Observable<Resume>) {
        result.subscribe((res: Resume) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Resume) {
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

    trackBasicInformationById(index: number, item: BasicInformation) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-resume-popup',
    template: ''
})
export class ResumePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private resumePopupService: ResumePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.resumePopupService
                    .open(ResumeDialogComponent as Component, params['id']);
            } else {
                this.resumePopupService
                    .open(ResumeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
