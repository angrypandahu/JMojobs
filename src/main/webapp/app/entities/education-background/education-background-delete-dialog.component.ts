import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { EducationBackground } from './education-background.model';
import { EducationBackgroundPopupService } from './education-background-popup.service';
import { EducationBackgroundService } from './education-background.service';

@Component({
    selector: 'jhi-education-background-delete-dialog',
    templateUrl: './education-background-delete-dialog.component.html'
})
export class EducationBackgroundDeleteDialogComponent {

    educationBackground: EducationBackground;

    constructor(
        private educationBackgroundService: EducationBackgroundService,
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
    selector: 'jhi-education-background-delete-popup',
    template: ''
})
export class EducationBackgroundDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private educationBackgroundPopupService: EducationBackgroundPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.educationBackgroundPopupService
                .open(EducationBackgroundDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
