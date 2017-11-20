import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { MLanguageMojobs } from './m-language-mojobs.model';
import { MLanguageMojobsService } from './m-language-mojobs.service';

@Component({
    selector: 'jhi-m-language-mojobs-detail',
    templateUrl: './m-language-mojobs-detail.component.html'
})
export class MLanguageMojobsDetailComponent implements OnInit, OnDestroy {

    mLanguage: MLanguageMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private mLanguageService: MLanguageMojobsService,
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
