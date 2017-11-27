import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobSubType } from './job-sub-type.model';
import { JobSubTypePopupService } from './job-sub-type-popup.service';
import { JobSubTypeService } from './job-sub-type.service';

@Component({
    selector: 'jhi-job-sub-type-delete-dialog',
    templateUrl: './job-sub-type-delete-dialog.component.html'
})
export class JobSubTypeDeleteDialogComponent {

    jobSubType: JobSubType;

    constructor(
        private jobSubTypeService: JobSubTypeService,
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
    selector: 'jhi-job-sub-type-delete-popup',
    template: ''
})
export class JobSubTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobSubTypePopupService: JobSubTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobSubTypePopupService
                .open(JobSubTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
