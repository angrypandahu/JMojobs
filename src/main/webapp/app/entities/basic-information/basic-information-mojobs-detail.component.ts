import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { BasicInformationMojobs } from './basic-information-mojobs.model';
import { BasicInformationMojobsService } from './basic-information-mojobs.service';

@Component({
    selector: 'jhi-basic-information-mojobs-detail',
    templateUrl: './basic-information-mojobs-detail.component.html'
})
export class BasicInformationMojobsDetailComponent implements OnInit, OnDestroy {

    basicInformation: BasicInformationMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private basicInformationService: BasicInformationMojobsService,
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
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
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
