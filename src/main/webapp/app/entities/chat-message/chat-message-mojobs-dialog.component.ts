import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ChatMessageMojobs } from './chat-message-mojobs.model';
import { ChatMessageMojobsPopupService } from './chat-message-mojobs-popup.service';
import { ChatMessageMojobsService } from './chat-message-mojobs.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-chat-message-mojobs-dialog',
    templateUrl: './chat-message-mojobs-dialog.component.html'
})
export class ChatMessageMojobsDialogComponent implements OnInit {

    chatMessage: ChatMessageMojobs;
    isSaving: boolean;

    users: User[];
    sendTimeDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private chatMessageService: ChatMessageMojobsService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.chatMessage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.chatMessageService.update(this.chatMessage));
        } else {
            this.subscribeToSaveResponse(
                this.chatMessageService.create(this.chatMessage));
        }
    }

    private subscribeToSaveResponse(result: Observable<ChatMessageMojobs>) {
        result.subscribe((res: ChatMessageMojobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ChatMessageMojobs) {
        this.eventManager.broadcast({ name: 'chatMessageListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-chat-message-mojobs-popup',
    template: ''
})
export class ChatMessageMojobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatMessagePopupService: ChatMessageMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.chatMessagePopupService
                    .open(ChatMessageMojobsDialogComponent as Component, params['id']);
            } else {
                this.chatMessagePopupService
                    .open(ChatMessageMojobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
