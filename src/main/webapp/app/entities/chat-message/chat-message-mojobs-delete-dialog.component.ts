import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ChatMessageMojobs } from './chat-message-mojobs.model';
import { ChatMessageMojobsPopupService } from './chat-message-mojobs-popup.service';
import { ChatMessageMojobsService } from './chat-message-mojobs.service';

@Component({
    selector: 'jhi-chat-message-mojobs-delete-dialog',
    templateUrl: './chat-message-mojobs-delete-dialog.component.html'
})
export class ChatMessageMojobsDeleteDialogComponent {

    chatMessage: ChatMessageMojobs;

    constructor(
        private chatMessageService: ChatMessageMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.chatMessageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'chatMessageListModification',
                content: 'Deleted an chatMessage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-chat-message-mojobs-delete-popup',
    template: ''
})
export class ChatMessageMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatMessagePopupService: ChatMessageMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.chatMessagePopupService
                .open(ChatMessageMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
