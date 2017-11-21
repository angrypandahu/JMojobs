import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { SchoolMojobs } from './school-mojobs.model';
import { SchoolMojobsService } from './school-mojobs.service';

@Component({
    selector: 'jhi-school-mojobs-detail',
    templateUrl: './school-mojobs-detail.component.html'
})
export class SchoolMojobsDetailComponent implements OnInit, OnDestroy {

    school: SchoolMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private schoolService: SchoolMojobsService,
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

    registerChangeInSchools() {
        this.eventSubscriber = this.eventManager.subscribe(
            'schoolListModification',
            (response) => this.load(this.school.id)
        );
    }
}
