import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Resume } from './resume.model';
import { ResumePopupService } from './resume-popup.service';
import { ResumeService } from './resume.service';

@Component({
    selector: 'jhi-resume-delete-dialog',
    templateUrl: './resume-delete-dialog.component.html'
})
export class ResumeDeleteDialogComponent {

    resume: Resume;

    constructor(
        private resumeService: ResumeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.resumeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'resumeListModification',
                content: 'Deleted an resume'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-resume-delete-popup',
    template: ''
})
export class ResumeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private resumePopupService: ResumePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.resumePopupService
                .open(ResumeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
