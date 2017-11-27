import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ApplyJobResume } from './apply-job-resume.model';
import { ApplyJobResumeService } from './apply-job-resume.service';

@Component({
    selector: 'jhi-apply-job-resume-detail',
    templateUrl: './apply-job-resume-detail.component.html'
})
export class ApplyJobResumeDetailComponent implements OnInit, OnDestroy {

    applyJobResume: ApplyJobResume;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private applyJobResumeService: ApplyJobResumeService,
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
