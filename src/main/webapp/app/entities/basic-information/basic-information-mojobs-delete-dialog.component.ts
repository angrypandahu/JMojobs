import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BasicInformationMojobs } from './basic-information-mojobs.model';
import { BasicInformationMojobsPopupService } from './basic-information-mojobs-popup.service';
import { BasicInformationMojobsService } from './basic-information-mojobs.service';

@Component({
    selector: 'jhi-basic-information-mojobs-delete-dialog',
    templateUrl: './basic-information-mojobs-delete-dialog.component.html'
})
export class BasicInformationMojobsDeleteDialogComponent {

    basicInformation: BasicInformationMojobs;

    constructor(
        private basicInformationService: BasicInformationMojobsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.basicInformationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'basicInformationListModification',
                content: 'Deleted an basicInformation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-basic-information-mojobs-delete-popup',
    template: ''
})
export class BasicInformationMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private basicInformationPopupService: BasicInformationMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.basicInformationPopupService
                .open(BasicInformationMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
