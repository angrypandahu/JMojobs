import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobAddressMojobs } from './job-address-mojobs.model';
import { JobAddressMojobsService } from './job-address-mojobs.service';

@Component({
    selector: 'jhi-job-address-mojobs-detail',
    templateUrl: './job-address-mojobs-detail.component.html'
})
export class JobAddressMojobsDetailComponent implements OnInit, OnDestroy {

    jobAddress: JobAddressMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobAddressService: JobAddressMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobAddresses();
    }

    load(id) {
        this.jobAddressService.find(id).subscribe((jobAddress) => {
            this.jobAddress = jobAddress;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobAddresses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobAddressListModification',
            (response) => this.load(this.jobAddress.id)
        );
    }
}
