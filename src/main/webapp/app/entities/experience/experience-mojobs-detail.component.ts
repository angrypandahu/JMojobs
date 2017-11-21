import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ExperienceMojobs } from './experience-mojobs.model';
import { ExperienceMojobsService } from './experience-mojobs.service';

@Component({
    selector: 'jhi-experience-mojobs-detail',
    templateUrl: './experience-mojobs-detail.component.html'
})
export class ExperienceMojobsDetailComponent implements OnInit, OnDestroy {

    experience: ExperienceMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private experienceService: ExperienceMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExperiences();
    }

    load(id) {
        this.experienceService.find(id).subscribe((experience) => {
            this.experience = experience;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExperiences() {
        this.eventSubscriber = this.eventManager.subscribe(
            'experienceListModification',
            (response) => this.load(this.experience.id)
        );
    }
}
