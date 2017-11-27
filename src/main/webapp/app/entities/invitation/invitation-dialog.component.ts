import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Invitation } from './invitation.model';
import { InvitationPopupService } from './invitation-popup.service';
import { InvitationService } from './invitation.service';
import { School, SchoolService } from '../school';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-invitation-dialog',
    templateUrl: './invitation-dialog.component.html'
})
export class InvitationDialogComponent implements OnInit {

    invitation: Invitation;
    isSaving: boolean;

    schools: School[];

    users: User[];
    fromDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private invitationService: InvitationService,
        private schoolService: SchoolService,
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

    private subscribeToSaveResponse(result: Observable<Invitation>) {
        result.subscribe((res: Invitation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Invitation) {
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

    trackSchoolById(index: number, item: School) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-invitation-popup',
    template: ''
})
export class InvitationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private invitationPopupService: InvitationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.invitationPopupService
                    .open(InvitationDialogComponent as Component, params['id']);
            } else {
                this.invitationPopupService
                    .open(InvitationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
