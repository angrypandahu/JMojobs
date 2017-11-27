import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ApplyJobResume } from './apply-job-resume.model';
import { ApplyJobResumePopupService } from './apply-job-resume-popup.service';
import { ApplyJobResumeService } from './apply-job-resume.service';

@Component({
    selector: 'jhi-apply-job-resume-delete-dialog',
    templateUrl: './apply-job-resume-delete-dialog.component.html'
})
export class ApplyJobResumeDeleteDialogComponent {

    applyJobResume: ApplyJobResume;

    constructor(
        private applyJobResumeService: ApplyJobResumeService,
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
    selector: 'jhi-apply-job-resume-delete-popup',
    template: ''
})
export class ApplyJobResumeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applyJobResumePopupService: ApplyJobResumePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.applyJobResumePopupService
                .open(ApplyJobResumeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
