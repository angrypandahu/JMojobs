import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AddressMojobsComponent } from './address-mojobs.component';
import { AddressMojobsDetailComponent } from './address-mojobs-detail.component';
import { AddressMojobsPopupComponent } from './address-mojobs-dialog.component';
import { AddressMojobsDeletePopupComponent } from './address-mojobs-delete-dialog.component';

export const addressRoute: Routes = [
    {
        path: 'address-mojobs',
        component: AddressMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Addresses'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'address-mojobs/:id',
        component: AddressMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Addresses'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const addressPopupRoute: Routes = [
    {
        path: 'address-mojobs-new',
        component: AddressMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Addresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'address-mojobs/:id/edit',
        component: AddressMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Addresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'address-mojobs/:id/delete',
        component: AddressMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Addresses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
