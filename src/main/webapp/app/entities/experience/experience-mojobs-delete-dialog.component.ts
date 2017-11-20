import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ExperienceMojobs } from './experience-mojobs.model';
import { ExperienceMojobsPopupService } from './experience-mojobs-popup.service';
import { ExperienceMojobsService } from './experience-mojobs.service';

@Component({
    selector: 'jhi-experience-mojobs-delete-dialog',
    templateUrl: './experience-mojobs-delete-dialog.component.html'
})
export class ExperienceMojobsDeleteDialogComponent {

    experience: ExperienceMojobs;

    constructor(
        private experienceService: ExperienceMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.experienceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'experienceListModification',
                content: 'Deleted an experience'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-experience-mojobs-delete-popup',
    template: ''
})
export class ExperienceMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private experiencePopupService: ExperienceMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.experiencePopupService
                .open(ExperienceMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
