import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MojobImageMojobs } from './mojob-image-mojobs.model';
import { MojobImageMojobsPopupService } from './mojob-image-mojobs-popup.service';
import { MojobImageMojobsService } from './mojob-image-mojobs.service';

@Component({
    selector: 'jhi-mojob-image-mojobs-delete-dialog',
    templateUrl: './mojob-image-mojobs-delete-dialog.component.html'
})
export class MojobImageMojobsDeleteDialogComponent {

    mojobImage: MojobImageMojobs;

    constructor(
        private mojobImageService: MojobImageMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mojobImageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mojobImageListModification',
                content: 'Deleted an mojobImage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mojob-image-mojobs-delete-popup',
    template: ''
})
export class MojobImageMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mojobImagePopupService: MojobImageMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mojobImagePopupService
                .open(MojobImageMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
