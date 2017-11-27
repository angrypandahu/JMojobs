import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Mjob } from './mjob.model';
import { MjobService } from './mjob.service';

@Component({
    selector: 'jhi-mjob-detail',
    templateUrl: './mjob-detail.component.html'
})
export class MjobDetailComponent implements OnInit, OnDestroy {

    mjob: Mjob;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private mjobService: MjobService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMjobs();
    }

    load(id) {
        this.mjobService.find(id).subscribe((mjob) => {
            this.mjob = mjob;
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

    registerChangeInMjobs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mjobListModification',
            (response) => this.load(this.mjob.id)
        );
    }
}
