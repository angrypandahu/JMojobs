import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CityMojobsComponent } from './city-mojobs.component';
import { CityMojobsDetailComponent } from './city-mojobs-detail.component';
import { CityMojobsPopupComponent } from './city-mojobs-dialog.component';
import { CityMojobsDeletePopupComponent } from './city-mojobs-delete-dialog.component';

export const cityRoute: Routes = [
    {
        path: 'city-mojobs',
        component: CityMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cities'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'city-mojobs/:id',
        component: CityMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cities'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cityPopupRoute: Routes = [
    {
        path: 'city-mojobs-new',
        component: CityMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cities'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'city-mojobs/:id/edit',
        component: CityMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cities'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'city-mojobs/:id/delete',
        component: CityMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cities'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
