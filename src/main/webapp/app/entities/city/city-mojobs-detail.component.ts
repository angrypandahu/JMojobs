import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { CityMojobs } from './city-mojobs.model';
import { CityMojobsService } from './city-mojobs.service';

@Component({
    selector: 'jhi-city-mojobs-detail',
    templateUrl: './city-mojobs-detail.component.html'
})
export class CityMojobsDetailComponent implements OnInit, OnDestroy {

    city: CityMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cityService: CityMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCities();
    }

    load(id) {
        this.cityService.find(id).subscribe((city) => {
            this.city = city;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cityListModification',
            (response) => this.load(this.city.id)
        );
    }
}
