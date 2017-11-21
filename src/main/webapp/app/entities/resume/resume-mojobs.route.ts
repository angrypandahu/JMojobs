import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ResumeMojobsComponent } from './resume-mojobs.component';
import { ResumeMojobsDetailComponent } from './resume-mojobs-detail.component';
import { ResumeMojobsPopupComponent } from './resume-mojobs-dialog.component';
import { ResumeMojobsDeletePopupComponent } from './resume-mojobs-delete-dialog.component';

export const resumeRoute: Routes = [
    {
        path: 'resume-mojobs',
        component: ResumeMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'resume-mojobs/:id',
        component: ResumeMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const resumePopupRoute: Routes = [
    {
        path: 'resume-mojobs-new',
        component: ResumeMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'resume-mojobs/:id/edit',
        component: ResumeMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'resume-mojobs/:id/delete',
        component: ResumeMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Resumes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
