import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { BasicInformation } from './basic-information.model';
import { BasicInformationService } from './basic-information.service';

@Component({
    selector: 'jhi-basic-information-detail',
    templateUrl: './basic-information-detail.component.html'
})
export class BasicInformationDetailComponent implements OnInit, OnDestroy {

    basicInformation: BasicInformation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private basicInformationService: BasicInformationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBasicInformations();
    }

    load(id) {
        this.basicInformationService.find(id).subscribe((basicInformation) => {
            this.basicInformation = basicInformation;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBasicInformations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'basicInformationListModification',
            (response) => this.load(this.basicInformation.id)
        );
    }
}
