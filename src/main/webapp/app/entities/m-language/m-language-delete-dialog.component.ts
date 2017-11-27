import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MLanguage } from './m-language.model';
import { MLanguagePopupService } from './m-language-popup.service';
import { MLanguageService } from './m-language.service';

@Component({
    selector: 'jhi-m-language-delete-dialog',
    templateUrl: './m-language-delete-dialog.component.html'
})
export class MLanguageDeleteDialogComponent {

    mLanguage: MLanguage;

    constructor(
        private mLanguageService: MLanguageService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mLanguageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mLanguageListModification',
                content: 'Deleted an mLanguage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-m-language-delete-popup',
    template: ''
})
export class MLanguageDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mLanguagePopupService: MLanguagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mLanguagePopupService
                .open(MLanguageDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
