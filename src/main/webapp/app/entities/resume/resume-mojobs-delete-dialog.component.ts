import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ResumeMojobs } from './resume-mojobs.model';
import { ResumeMojobsPopupService } from './resume-mojobs-popup.service';
import { ResumeMojobsService } from './resume-mojobs.service';

@Component({
    selector: 'jhi-resume-mojobs-delete-dialog',
    templateUrl: './resume-mojobs-delete-dialog.component.html'
})
export class ResumeMojobsDeleteDialogComponent {

    resume: ResumeMojobs;

    constructor(
        private resumeService: ResumeMojobsService,
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
    selector: 'jhi-resume-mojobs-delete-popup',
    template: ''
})
export class ResumeMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private resumePopupService: ResumeMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.resumePopupService
                .open(ResumeMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
