import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ProfessionalDevelopmentMojobs } from './professional-development-mojobs.model';
import { ProfessionalDevelopmentMojobsService } from './professional-development-mojobs.service';

@Component({
    selector: 'jhi-professional-development-mojobs-detail',
    templateUrl: './professional-development-mojobs-detail.component.html'
})
export class ProfessionalDevelopmentMojobsDetailComponent implements OnInit, OnDestroy {

    professionalDevelopment: ProfessionalDevelopmentMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private professionalDevelopmentService: ProfessionalDevelopmentMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProfessionalDevelopments();
    }

    load(id) {
        this.professionalDevelopmentService.find(id).subscribe((professionalDevelopment) => {
            this.professionalDevelopment = professionalDevelopment;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProfessionalDevelopments() {
        this.eventSubscriber = this.eventManager.subscribe(
            'professionalDevelopmentListModification',
            (response) => this.load(this.professionalDevelopment.id)
        );
    }
}
