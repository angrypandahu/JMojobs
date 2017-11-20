import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobAddressMojobs } from './job-address-mojobs.model';
import { JobAddressMojobsPopupService } from './job-address-mojobs-popup.service';
import { JobAddressMojobsService } from './job-address-mojobs.service';

@Component({
    selector: 'jhi-job-address-mojobs-delete-dialog',
    templateUrl: './job-address-mojobs-delete-dialog.component.html'
})
export class JobAddressMojobsDeleteDialogComponent {

    jobAddress: JobAddressMojobs;

    constructor(
        private jobAddressService: JobAddressMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobAddressService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jobAddressListModification',
                content: 'Deleted an jobAddress'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-address-mojobs-delete-popup',
    template: ''
})
export class JobAddressMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobAddressPopupService: JobAddressMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobAddressPopupService
                .open(JobAddressMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
