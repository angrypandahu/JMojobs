import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TownMojobs } from './town-mojobs.model';
import { TownMojobsPopupService } from './town-mojobs-popup.service';
import { TownMojobsService } from './town-mojobs.service';

@Component({
    selector: 'jhi-town-mojobs-delete-dialog',
    templateUrl: './town-mojobs-delete-dialog.component.html'
})
export class TownMojobsDeleteDialogComponent {

    town: TownMojobs;

    constructor(
        private townService: TownMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.townService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'townListModification',
                content: 'Deleted an town'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-town-mojobs-delete-popup',
    template: ''
})
export class TownMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private townPopupService: TownMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.townPopupService
                .open(TownMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
