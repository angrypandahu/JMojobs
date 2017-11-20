import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { InvitationMojobs } from './invitation-mojobs.model';
import { InvitationMojobsPopupService } from './invitation-mojobs-popup.service';
import { InvitationMojobsService } from './invitation-mojobs.service';

@Component({
    selector: 'jhi-invitation-mojobs-delete-dialog',
    templateUrl: './invitation-mojobs-delete-dialog.component.html'
})
export class InvitationMojobsDeleteDialogComponent {

    invitation: InvitationMojobs;

    constructor(
        private invitationService: InvitationMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.invitationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'invitationListModification',
                content: 'Deleted an invitation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-invitation-mojobs-delete-popup',
    template: ''
})
export class InvitationMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private invitationPopupService: InvitationMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.invitationPopupService
                .open(InvitationMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
