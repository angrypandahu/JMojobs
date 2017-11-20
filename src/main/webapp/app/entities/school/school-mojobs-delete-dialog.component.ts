import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SchoolMojobs } from './school-mojobs.model';
import { SchoolMojobsPopupService } from './school-mojobs-popup.service';
import { SchoolMojobsService } from './school-mojobs.service';

@Component({
    selector: 'jhi-school-mojobs-delete-dialog',
    templateUrl: './school-mojobs-delete-dialog.component.html'
})
export class SchoolMojobsDeleteDialogComponent {

    school: SchoolMojobs;

    constructor(
        private schoolService: SchoolMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.schoolService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'schoolListModification',
                content: 'Deleted an school'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-school-mojobs-delete-popup',
    template: ''
})
export class SchoolMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private schoolPopupService: SchoolMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.schoolPopupService
                .open(SchoolMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
