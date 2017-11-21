import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ExperienceMojobs } from './experience-mojobs.model';
import { ExperienceMojobsService } from './experience-mojobs.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-experience-mojobs',
    templateUrl: './experience-mojobs.component.html'
})
export class ExperienceMojobsComponent implements OnInit, OnDestroy {
experiences: ExperienceMojobs[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private experienceService: ExperienceMojobsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.experienceService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.experiences = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.experienceService.query().subscribe(
            (res: ResponseWrapper) => {
                this.experiences = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInExperiences();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ExperienceMojobs) {
        return item.id;
    }
    registerChangeInExperiences() {
        this.eventSubscriber = this.eventManager.subscribe('experienceListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
