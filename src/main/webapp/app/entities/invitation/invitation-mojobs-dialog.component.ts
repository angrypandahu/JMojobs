import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { InvitationMojobs } from './invitation-mojobs.model';
import { InvitationMojobsPopupService } from './invitation-mojobs-popup.service';
import { InvitationMojobsService } from './invitation-mojobs.service';
import { SchoolMojobs, SchoolMojobsService } from '../school';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-invitation-mojobs-dialog',
    templateUrl: './invitation-mojobs-dialog.component.html'
})
export class InvitationMojobsDialogComponent implements OnInit {

    invitation: InvitationMojobs;
    isSaving: boolean;

    schools: SchoolMojobs[];

    users: User[];
    fromDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private invitationService: InvitationMojobsService,
        private schoolService: SchoolMojobsService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.schoolService.query()
            .subscribe((res: ResponseWrapper) => { this.schools = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.invitation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.invitationService.update(this.invitation));
        } else {
            this.subscribeToSaveResponse(
                this.invitationService.create(this.invitation));
        }
    }

    private subscribeToSaveResponse(result: Observable<InvitationMojobs>) {
        result.subscribe((res: InvitationMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: InvitationMojobs) {
        this.eventManager.broadcast({ name: 'invitationListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-invitation-mojobs-popup',
    template: ''
})
export class InvitationMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private invitationPopupService: InvitationMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.invitationPopupService
                    .open(InvitationMojobsDialogComponent as Component, params['id']);
            } else {
                this.invitationPopupService
                    .open(InvitationMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
