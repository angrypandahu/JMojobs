import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SchoolAddressMojobs } from './school-address-mojobs.model';
import { SchoolAddressMojobsService } from './school-address-mojobs.service';

@Component({
    selector: 'jhi-school-address-mojobs-detail',
    templateUrl: './school-address-mojobs-detail.component.html'
})
export class SchoolAddressMojobsDetailComponent implements OnInit, OnDestroy {

    schoolAddress: SchoolAddressMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private schoolAddressService: SchoolAddressMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSchoolAddresses();
    }

    load(id) {
        this.schoolAddressService.find(id).subscribe((schoolAddress) => {
            this.schoolAddress = schoolAddress;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSchoolAddresses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'schoolAddressListModification',
            (response) => this.load(this.schoolAddress.id)
        );
    }
}
