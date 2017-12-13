import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ResumeComponent } from './resume.component';
import { ResumeDetailComponent } from './resume-detail.component';
import { ResumePopupComponent } from './resume-dialog.component';
import { ResumeDeletePopupComponent } from './resume-delete-dialog.component';

@Injectable()
export class ResumeResolvePagingParams implements Resolve<any> {

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

export const resumeRoute: Routes = [
    {
        path: 'resume',
        component: ResumeComponent,
        resolve: {
            'pagingParams': ResumeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'resume/:id',
        component: ResumeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const resumePopupRoute: Routes = [
    {
        path: 'resume-new',
        component: ResumePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'resume/:id/edit',
        component: ResumePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'resume/:id/delete',
        component: ResumeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
