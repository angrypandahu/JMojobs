import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Mjob } from './mjob.model';
import { MjobPopupService } from './mjob-popup.service';
import { MjobService } from './mjob.service';

@Component({
    selector: 'jhi-mjob-delete-dialog',
    templateUrl: './mjob-delete-dialog.component.html'
})
export class MjobDeleteDialogComponent {

    mjob: Mjob;

    constructor(
        private mjobService: MjobService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mjobService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mjobListModification',
                content: 'Deleted an mjob'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mjob-delete-popup',
    template: ''
})
export class MjobDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mjobPopupService: MjobPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mjobPopupService
                .open(MjobDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
