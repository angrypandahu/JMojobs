import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ApplyJobResumeComponent } from './apply-job-resume.component';
import { ApplyJobResumeDetailComponent } from './apply-job-resume-detail.component';
import { ApplyJobResumePopupComponent } from './apply-job-resume-dialog.component';
import { ApplyJobResumeDeletePopupComponent } from './apply-job-resume-delete-dialog.component';

@Injectable()
export class ApplyJobResumeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const applyJobResumeRoute: Routes = [
    {
        path: 'apply-job-resume',
        component: ApplyJobResumeComponent,
        resolve: {
            'pagingParams': ApplyJobResumeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'apply-job-resume/:id',
        component: ApplyJobResumeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const applyJobResumePopupRoute: Routes = [
    {
        path: 'apply-job-resume-new',
        component: ApplyJobResumePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'apply-job-resume/:id/edit',
        component: ApplyJobResumePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'apply-job-resume/:id/delete',
        component: ApplyJobResumeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
