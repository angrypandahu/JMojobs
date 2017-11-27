import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobSubTypeComponent } from './job-sub-type.component';
import { JobSubTypeDetailComponent } from './job-sub-type-detail.component';
import { JobSubTypePopupComponent } from './job-sub-type-dialog.component';
import { JobSubTypeDeletePopupComponent } from './job-sub-type-delete-dialog.component';

export const jobSubTypeRoute: Routes = [
    {
        path: 'job-sub-type',
        component: JobSubTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-sub-type/:id',
        component: JobSubTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobSubTypePopupRoute: Routes = [
    {
        path: 'job-sub-type-new',
        component: JobSubTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-sub-type/:id/edit',
        component: JobSubTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-sub-type/:id/delete',
        component: JobSubTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobSubTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
