import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ApplyJobResumeMojobsComponent } from './apply-job-resume-mojobs.component';
import { ApplyJobResumeMojobsDetailComponent } from './apply-job-resume-mojobs-detail.component';
import { ApplyJobResumeMojobsPopupComponent } from './apply-job-resume-mojobs-dialog.component';
import { ApplyJobResumeMojobsDeletePopupComponent } from './apply-job-resume-mojobs-delete-dialog.component';

@Injectable()
export class ApplyJobResumeMojobsResolvePagingParams implements Resolve<any> {

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
        path: 'apply-job-resume-mojobs',
        component: ApplyJobResumeMojobsComponent,
        resolve: {
            'pagingParams': ApplyJobResumeMojobsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'apply-job-resume-mojobs/:id',
        component: ApplyJobResumeMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const applyJobResumePopupRoute: Routes = [
    {
        path: 'apply-job-resume-mojobs-new',
        component: ApplyJobResumeMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'apply-job-resume-mojobs/:id/edit',
        component: ApplyJobResumeMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'apply-job-resume-mojobs/:id/delete',
        component: ApplyJobResumeMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ApplyJobResumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
