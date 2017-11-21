import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MjobMojobs } from './mjob-mojobs.model';
import { MjobMojobsPopupService } from './mjob-mojobs-popup.service';
import { MjobMojobsService } from './mjob-mojobs.service';

@Component({
    selector: 'jhi-mjob-mojobs-delete-dialog',
    templateUrl: './mjob-mojobs-delete-dialog.component.html'
})
export class MjobMojobsDeleteDialogComponent {

    mjob: MjobMojobs;

    constructor(
        private mjobService: MjobMojobsService,
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
    selector: 'jhi-mjob-mojobs-delete-popup',
    template: ''
})
export class MjobMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mjobPopupService: MjobMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mjobPopupService
                .open(MjobMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
