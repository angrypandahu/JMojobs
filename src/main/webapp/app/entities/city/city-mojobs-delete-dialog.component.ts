import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CityMojobs } from './city-mojobs.model';
import { CityMojobsPopupService } from './city-mojobs-popup.service';
import { CityMojobsService } from './city-mojobs.service';

@Component({
    selector: 'jhi-city-mojobs-delete-dialog',
    templateUrl: './city-mojobs-delete-dialog.component.html'
})
export class CityMojobsDeleteDialogComponent {

    city: CityMojobs;

    constructor(
        private cityService: CityMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cityService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cityListModification',
                content: 'Deleted an city'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-city-mojobs-delete-popup',
    template: ''
})
export class CityMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cityPopupService: CityMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cityPopupService
                .open(CityMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
