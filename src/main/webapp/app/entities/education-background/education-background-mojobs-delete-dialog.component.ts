import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { EducationBackgroundMojobs } from './education-background-mojobs.model';
import { EducationBackgroundMojobsPopupService } from './education-background-mojobs-popup.service';
import { EducationBackgroundMojobsService } from './education-background-mojobs.service';

@Component({
    selector: 'jhi-education-background-mojobs-delete-dialog',
    templateUrl: './education-background-mojobs-delete-dialog.component.html'
})
export class EducationBackgroundMojobsDeleteDialogComponent {

    educationBackground: EducationBackgroundMojobs;

    constructor(
        private educationBackgroundService: EducationBackgroundMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.educationBackgroundService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'educationBackgroundListModification',
                content: 'Deleted an educationBackground'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-education-background-mojobs-delete-popup',
    template: ''
})
export class EducationBackgroundMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private educationBackgroundPopupService: EducationBackgroundMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.educationBackgroundPopupService
                .open(EducationBackgroundMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
