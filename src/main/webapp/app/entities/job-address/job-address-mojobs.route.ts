import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobAddressMojobsComponent } from './job-address-mojobs.component';
import { JobAddressMojobsDetailComponent } from './job-address-mojobs-detail.component';
import { JobAddressMojobsPopupComponent } from './job-address-mojobs-dialog.component';
import { JobAddressMojobsDeletePopupComponent } from './job-address-mojobs-delete-dialog.component';

export const jobAddressRoute: Routes = [
    {
        path: 'job-address-mojobs',
        component: JobAddressMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobAddresses'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-address-mojobs/:id',
        component: JobAddressMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobAddresses'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobAddressPopupRoute: Routes = [
    {
        path: 'job-address-mojobs-new',
        component: JobAddressMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobAddresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-address-mojobs/:id/edit',
        component: JobAddressMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobAddresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-address-mojobs/:id/delete',
        component: JobAddressMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JobAddresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
