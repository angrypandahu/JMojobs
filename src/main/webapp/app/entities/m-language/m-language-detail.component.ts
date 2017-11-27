import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { MLanguage } from './m-language.model';
import { MLanguageService } from './m-language.service';

@Component({
    selector: 'jhi-m-language-detail',
    templateUrl: './m-language-detail.component.html'
})
export class MLanguageDetailComponent implements OnInit, OnDestroy {

    mLanguage: MLanguage;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private mLanguageService: MLanguageService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMLanguages();
    }

    load(id) {
        this.mLanguageService.find(id).subscribe((mLanguage) => {
            this.mLanguage = mLanguage;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMLanguages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mLanguageListModification',
            (response) => this.load(this.mLanguage.id)
        );
    }
}
