import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SchoolAddressMojobs } from './school-address-mojobs.model';
import { SchoolAddressMojobsPopupService } from './school-address-mojobs-popup.service';
import { SchoolAddressMojobsService } from './school-address-mojobs.service';

@Component({
    selector: 'jhi-school-address-mojobs-delete-dialog',
    templateUrl: './school-address-mojobs-delete-dialog.component.html'
})
export class SchoolAddressMojobsDeleteDialogComponent {

    schoolAddress: SchoolAddressMojobs;

    constructor(
        private schoolAddressService: SchoolAddressMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.schoolAddressService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'schoolAddressListModification',
                content: 'Deleted an schoolAddress'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-school-address-mojobs-delete-popup',
    template: ''
})
export class SchoolAddressMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private schoolAddressPopupService: SchoolAddressMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.schoolAddressPopupService
                .open(SchoolAddressMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
