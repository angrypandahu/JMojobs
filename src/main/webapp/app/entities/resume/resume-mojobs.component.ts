import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ResumeMojobs } from './resume-mojobs.model';
import { ResumeMojobsService } from './resume-mojobs.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-resume-mojobs',
    templateUrl: './resume-mojobs.component.html'
})
export class ResumeMojobsComponent implements OnInit, OnDestroy {
resumes: ResumeMojobs[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private resumeService: ResumeMojobsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.resumeService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.resumes = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.resumeService.query().subscribe(
            (res: ResponseWrapper) => {
                this.resumes = res.json;
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
        this.registerChangeInResumes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ResumeMojobs) {
        return item.id;
    }
    registerChangeInResumes() {
        this.eventSubscriber = this.eventManager.subscribe('resumeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
