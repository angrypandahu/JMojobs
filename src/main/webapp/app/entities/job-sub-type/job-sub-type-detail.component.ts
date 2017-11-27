import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobSubType } from './job-sub-type.model';
import { JobSubTypeService } from './job-sub-type.service';

@Component({
    selector: 'jhi-job-sub-type-detail',
    templateUrl: './job-sub-type-detail.component.html'
})
export class JobSubTypeDetailComponent implements OnInit, OnDestroy {

    jobSubType: JobSubType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobSubTypeService: JobSubTypeService,
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
