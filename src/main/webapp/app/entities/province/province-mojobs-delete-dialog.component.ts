import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProvinceMojobs } from './province-mojobs.model';
import { ProvinceMojobsPopupService } from './province-mojobs-popup.service';
import { ProvinceMojobsService } from './province-mojobs.service';

@Component({
    selector: 'jhi-province-mojobs-delete-dialog',
    templateUrl: './province-mojobs-delete-dialog.component.html'
})
export class ProvinceMojobsDeleteDialogComponent {

    province: ProvinceMojobs;

    constructor(
        private provinceService: ProvinceMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.provinceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'provinceListModification',
                content: 'Deleted an province'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-province-mojobs-delete-popup',
    template: ''
})
export class ProvinceMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private provincePopupService: ProvinceMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.provincePopupService
                .open(ProvinceMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
