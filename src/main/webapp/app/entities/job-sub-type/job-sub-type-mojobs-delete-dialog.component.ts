import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobSubTypeMojobs } from './job-sub-type-mojobs.model';
import { JobSubTypeMojobsPopupService } from './job-sub-type-mojobs-popup.service';
import { JobSubTypeMojobsService } from './job-sub-type-mojobs.service';

@Component({
    selector: 'jhi-job-sub-type-mojobs-delete-dialog',
    templateUrl: './job-sub-type-mojobs-delete-dialog.component.html'
})
export class JobSubTypeMojobsDeleteDialogComponent {

    jobSubType: JobSubTypeMojobs;

    constructor(
        private jobSubTypeService: JobSubTypeMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobSubTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jobSubTypeListModification',
                content: 'Deleted an jobSubType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-sub-type-mojobs-delete-popup',
    template: ''
})
export class JobSubTypeMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobSubTypePopupService: JobSubTypeMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobSubTypePopupService
                .open(JobSubTypeMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
