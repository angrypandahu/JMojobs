import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TownMojobsComponent } from './town-mojobs.component';
import { TownMojobsDetailComponent } from './town-mojobs-detail.component';
import { TownMojobsPopupComponent } from './town-mojobs-dialog.component';
import { TownMojobsDeletePopupComponent } from './town-mojobs-delete-dialog.component';

export const townRoute: Routes = [
    {
        path: 'town-mojobs',
        component: TownMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Towns'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'town-mojobs/:id',
        component: TownMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Towns'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const townPopupRoute: Routes = [
    {
        path: 'town-mojobs-new',
        component: TownMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Towns'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'town-mojobs/:id/edit',
        component: TownMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Towns'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'town-mojobs/:id/delete',
        component: TownMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Towns'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
