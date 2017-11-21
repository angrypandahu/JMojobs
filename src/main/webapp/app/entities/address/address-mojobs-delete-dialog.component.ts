import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AddressMojobs } from './address-mojobs.model';
import { AddressMojobsPopupService } from './address-mojobs-popup.service';
import { AddressMojobsService } from './address-mojobs.service';

@Component({
    selector: 'jhi-address-mojobs-delete-dialog',
    templateUrl: './address-mojobs-delete-dialog.component.html'
})
export class AddressMojobsDeleteDialogComponent {

    address: AddressMojobs;

    constructor(
        private addressService: AddressMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.addressService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'addressListModification',
                content: 'Deleted an address'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-address-mojobs-delete-popup',
    template: ''
})
export class AddressMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private addressPopupService: AddressMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.addressPopupService
                .open(AddressMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
