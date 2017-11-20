import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MLanguageMojobs } from './m-language-mojobs.model';
import { MLanguageMojobsPopupService } from './m-language-mojobs-popup.service';
import { MLanguageMojobsService } from './m-language-mojobs.service';

@Component({
    selector: 'jhi-m-language-mojobs-delete-dialog',
    templateUrl: './m-language-mojobs-delete-dialog.component.html'
})
export class MLanguageMojobsDeleteDialogComponent {

    mLanguage: MLanguageMojobs;

    constructor(
        private mLanguageService: MLanguageMojobsService,
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
    selector: 'jhi-m-language-mojobs-delete-popup',
    template: ''
})
export class MLanguageMojobsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mLanguagePopupService: MLanguageMojobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mLanguagePopupService
                .open(MLanguageMojobsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
