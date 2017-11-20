import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProvinceMojobsComponent } from './province-mojobs.component';
import { ProvinceMojobsDetailComponent } from './province-mojobs-detail.component';
import { ProvinceMojobsPopupComponent } from './province-mojobs-dialog.component';
import { ProvinceMojobsDeletePopupComponent } from './province-mojobs-delete-dialog.component';

export const provinceRoute: Routes = [
    {
        path: 'province-mojobs',
        component: ProvinceMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Provinces'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'province-mojobs/:id',
        component: ProvinceMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Provinces'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const provincePopupRoute: Routes = [
    {
        path: 'province-mojobs-new',
        component: ProvinceMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Provinces'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'province-mojobs/:id/edit',
        component: ProvinceMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Provinces'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'province-mojobs/:id/delete',
        component: ProvinceMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Provinces'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
