import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ApplyJobResumeMojobs } from './apply-job-resume-mojobs.model';
import { ApplyJobResumeMojobsPopupService } from './apply-job-resume-mojobs-popup.service';
import { ApplyJobResumeMojobsService } from './apply-job-resume-mojobs.service';

@Component({
    selector: 'jhi-apply-job-resume-mojobs-delete-dialog',
    templateUrl: './apply-job-resume-mojobs-delete-dialog.component.html'
})
export class ApplyJobResumeMojobsDeleteDialogComponent {

    applyJobResume: ApplyJobResumeMojobs;

    constructor(
        private applyJobResumeService: ApplyJobResumeMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.applyJobResumeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'applyJobResumeListModification',
                content: 'Deleted an applyJobResume'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-apply-job-resume-mojobs-delete-popup',
    template: ''
})
export class ApplyJobResumeMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applyJobResumePopupService: ApplyJobResumeMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.applyJobResumePopupService
                .open(ApplyJobResumeMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
