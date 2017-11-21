import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { MojobImageMojobs } from './mojob-image-mojobs.model';
import { MojobImageMojobsService } from './mojob-image-mojobs.service';

@Component({
    selector: 'jhi-mojob-image-mojobs-detail',
    templateUrl: './mojob-image-mojobs-detail.component.html'
})
export class MojobImageMojobsDetailComponent implements OnInit, OnDestroy {

    mojobImage: MojobImageMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private mojobImageService: MojobImageMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMojobImages();
    }

    load(id) {
        this.mojobImageService.find(id).subscribe((mojobImage) => {
            this.mojobImage = mojobImage;
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

    registerChangeInMojobImages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mojobImageListModification',
            (response) => this.load(this.mojobImage.id)
        );
    }
}
