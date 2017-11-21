import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobSubTypeMojobs } from './job-sub-type-mojobs.model';
import { JobSubTypeMojobsService } from './job-sub-type-mojobs.service';

@Component({
    selector: 'jhi-job-sub-type-mojobs-detail',
    templateUrl: './job-sub-type-mojobs-detail.component.html'
})
export class JobSubTypeMojobsDetailComponent implements OnInit, OnDestroy {

    jobSubType: JobSubTypeMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobSubTypeService: JobSubTypeMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobSubTypes();
    }

    load(id) {
        this.jobSubTypeService.find(id).subscribe((jobSubType) => {
            this.jobSubType = jobSubType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobSubTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobSubTypeListModification',
            (response) => this.load(this.jobSubType.id)
        );
    }
}
