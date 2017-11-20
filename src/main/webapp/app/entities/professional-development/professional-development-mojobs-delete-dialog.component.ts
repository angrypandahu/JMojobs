import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProfessionalDevelopmentMojobs } from './professional-development-mojobs.model';
import { ProfessionalDevelopmentMojobsPopupService } from './professional-development-mojobs-popup.service';
import { ProfessionalDevelopmentMojobsService } from './professional-development-mojobs.service';

@Component({
    selector: 'jhi-professional-development-mojobs-delete-dialog',
    templateUrl: './professional-development-mojobs-delete-dialog.component.html'
})
export class ProfessionalDevelopmentMojobsDeleteDialogComponent {

    professionalDevelopment: ProfessionalDevelopmentMojobs;

    constructor(
        private professionalDevelopmentService: ProfessionalDevelopmentMojobsService,
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
    selector: 'jhi-professional-development-mojobs-delete-popup',
    template: ''
})
export class ProfessionalDevelopmentMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private professionalDevelopmentPopupService: ProfessionalDevelopmentMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.professionalDevelopmentPopupService
                .open(ProfessionalDevelopmentMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
