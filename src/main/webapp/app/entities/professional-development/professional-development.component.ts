import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ProfessionalDevelopment } from './professional-development.model';
import { ProfessionalDevelopmentService } from './professional-development.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-professional-development',
    templateUrl: './professional-development.component.html'
})
export class ProfessionalDevelopmentComponent implements OnInit, OnDestroy {
professionalDevelopments: ProfessionalDevelopment[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private professionalDevelopmentService: ProfessionalDevelopmentService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.professionalDevelopmentService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.professionalDevelopments = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.professionalDevelopmentService.query().subscribe(
            (res: ResponseWrapper) => {
                this.professionalDevelopments = res.json;
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
        this.registerChangeInProfessionalDevelopments();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProfessionalDevelopment) {
        return item.id;
    }
    registerChangeInProfessionalDevelopments() {
        this.eventSubscriber = this.eventManager.subscribe('professionalDevelopmentListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
