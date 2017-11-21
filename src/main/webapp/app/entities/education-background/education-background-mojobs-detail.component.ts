import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { EducationBackgroundMojobs } from './education-background-mojobs.model';
import { EducationBackgroundMojobsService } from './education-background-mojobs.service';

@Component({
    selector: 'jhi-education-background-mojobs-detail',
    templateUrl: './education-background-mojobs-detail.component.html'
})
export class EducationBackgroundMojobsDetailComponent implements OnInit, OnDestroy {

    educationBackground: EducationBackgroundMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private educationBackgroundService: EducationBackgroundMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEducationBackgrounds();
    }

    load(id) {
        this.educationBackgroundService.find(id).subscribe((educationBackground) => {
            this.educationBackground = educationBackground;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEducationBackgrounds() {
        this.eventSubscriber = this.eventManager.subscribe(
            'educationBackgroundListModification',
            (response) => this.load(this.educationBackground.id)
        );
    }
}
