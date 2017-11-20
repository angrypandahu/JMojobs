import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobSubTypeMojobsComponent } from './job-sub-type-mojobs.component';
import { JobSubTypeMojobsDetailComponent } from './job-sub-type-mojobs-detail.component';
import { JobSubTypeMojobsPopupComponent } from './job-sub-type-mojobs-dialog.component';
import { JobSubTypeMojobsDeletePopupComponent } from './job-sub-type-mojobs-delete-dialog.component';

export const jobSubTypeRoute: Routes = [
    {
        path: 'job-sub-type-mojobs',
        component: JobSubTypeMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-sub-type-mojobs/:id',
        component: JobSubTypeMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobSubTypePopupRoute: Routes = [
    {
        path: 'job-sub-type-mojobs-new',
        component: JobSubTypeMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-sub-type-mojobs/:id/edit',
        component: JobSubTypeMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-sub-type-mojobs/:id/delete',
        component: JobSubTypeMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
