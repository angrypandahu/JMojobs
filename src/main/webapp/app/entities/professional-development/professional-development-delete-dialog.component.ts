import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProfessionalDevelopment } from './professional-development.model';
import { ProfessionalDevelopmentPopupService } from './professional-development-popup.service';
import { ProfessionalDevelopmentService } from './professional-development.service';

@Component({
    selector: 'jhi-professional-development-delete-dialog',
    templateUrl: './professional-development-delete-dialog.component.html'
})
export class ProfessionalDevelopmentDeleteDialogComponent {

    professionalDevelopment: ProfessionalDevelopment;

    constructor(
        private professionalDevelopmentService: ProfessionalDevelopmentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.professionalDevelopmentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'professionalDevelopmentListModification',
                content: 'Deleted an professionalDevelopment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-professional-development-delete-popup',
    template: ''
})
export class ProfessionalDevelopmentDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private professionalDevelopmentPopupService: ProfessionalDevelopmentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.professionalDevelopmentPopupService
                .open(ProfessionalDevelopmentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
