import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SchoolAddressMojobsComponent } from './school-address-mojobs.component';
import { SchoolAddressMojobsDetailComponent } from './school-address-mojobs-detail.component';
import { SchoolAddressMojobsPopupComponent } from './school-address-mojobs-dialog.component';
import { SchoolAddressMojobsDeletePopupComponent } from './school-address-mojobs-delete-dialog.component';

export const schoolAddressRoute: Routes = [
    {
        path: 'school-address-mojobs',
        component: SchoolAddressMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SchoolAddresses'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'school-address-mojobs/:id',
        component: SchoolAddressMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SchoolAddresses'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const schoolAddressPopupRoute: Routes = [
    {
        path: 'school-address-mojobs-new',
        component: SchoolAddressMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SchoolAddresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'school-address-mojobs/:id/edit',
        component: SchoolAddressMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SchoolAddresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'school-address-mojobs/:id/delete',
        component: SchoolAddressMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SchoolAddresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
