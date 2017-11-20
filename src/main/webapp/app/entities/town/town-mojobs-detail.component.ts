import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TownMojobs } from './town-mojobs.model';
import { TownMojobsService } from './town-mojobs.service';

@Component({
    selector: 'jhi-town-mojobs-detail',
    templateUrl: './town-mojobs-detail.component.html'
})
export class TownMojobsDetailComponent implements OnInit, OnDestroy {

    town: TownMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private townService: TownMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTowns();
    }

    load(id) {
        this.townService.find(id).subscribe((town) => {
            this.town = town;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTowns() {
        this.eventSubscriber = this.eventManager.subscribe(
            'townListModification',
            (response) => this.load(this.town.id)
        );
    }
}
