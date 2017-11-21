import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { EducationBackgroundMojobs } from './education-background-mojobs.model';
import { EducationBackgroundMojobsService } from './education-background-mojobs.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-education-background-mojobs',
    templateUrl: './education-background-mojobs.component.html'
})
export class EducationBackgroundMojobsComponent implements OnInit, OnDestroy {
educationBackgrounds: EducationBackgroundMojobs[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private educationBackgroundService: EducationBackgroundMojobsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.educationBackgroundService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.educationBackgrounds = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.educationBackgroundService.query().subscribe(
            (res: ResponseWrapper) => {
                this.educationBackgrounds = res.json;
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
        this.registerChangeInEducationBackgrounds();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: EducationBackgroundMojobs) {
        return item.id;
    }
    registerChangeInEducationBackgrounds() {
        this.eventSubscriber = this.eventManager.subscribe('educationBackgroundListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
