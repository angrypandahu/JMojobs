import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { School } from './school.model';
import { SchoolService } from './school.service';

@Component({
    selector: 'jhi-school-detail',
    templateUrl: './school-detail.component.html'
})
export class SchoolDetailComponent implements OnInit, OnDestroy {

    school: School;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private schoolService: SchoolService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSchools();
    }

    load(id) {
        this.schoolService.find(id).subscribe((school) => {
            this.school = school;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSchools() {
        this.eventSubscriber = this.eventManager.subscribe(
            'schoolListModification',
            (response) => this.load(this.school.id)
        );
    }
}
