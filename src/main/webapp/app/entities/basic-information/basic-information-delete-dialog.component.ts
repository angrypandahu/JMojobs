import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BasicInformation } from './basic-information.model';
import { BasicInformationPopupService } from './basic-information-popup.service';
import { BasicInformationService } from './basic-information.service';

@Component({
    selector: 'jhi-basic-information-delete-dialog',
    templateUrl: './basic-information-delete-dialog.component.html'
})
export class BasicInformationDeleteDialogComponent {

    basicInformation: BasicInformation;

    constructor(
        private basicInformationService: BasicInformationService,
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
    selector: 'jhi-basic-information-delete-popup',
    template: ''
})
export class BasicInformationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private basicInformationPopupService: BasicInformationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.basicInformationPopupService
                .open(BasicInformationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
