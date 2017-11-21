import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ApplyJobResumeMojobs } from './apply-job-resume-mojobs.model';
import { ApplyJobResumeMojobsService } from './apply-job-resume-mojobs.service';

@Component({
    selector: 'jhi-apply-job-resume-mojobs-detail',
    templateUrl: './apply-job-resume-mojobs-detail.component.html'
})
export class ApplyJobResumeMojobsDetailComponent implements OnInit, OnDestroy {

    applyJobResume: ApplyJobResumeMojobs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private applyJobResumeService: ApplyJobResumeMojobsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApplyJobResumes();
    }

    load(id) {
        this.applyJobResumeService.find(id).subscribe((applyJobResume) => {
            this.applyJobResume = applyJobResume;
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

    registerChangeInApplyJobResumes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'applyJobResumeListModification',
            (response) => this.load(this.applyJobResume.id)
        );
    }
}
