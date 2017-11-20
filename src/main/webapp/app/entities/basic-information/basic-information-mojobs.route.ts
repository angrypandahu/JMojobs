import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BasicInformationMojobsComponent } from './basic-information-mojobs.component';
import { BasicInformationMojobsDetailComponent } from './basic-information-mojobs-detail.component';
import { BasicInformationMojobsPopupComponent } from './basic-information-mojobs-dialog.component';
import { BasicInformationMojobsDeletePopupComponent } from './basic-information-mojobs-delete-dialog.component';

export const basicInformationRoute: Routes = [
    {
        path: 'basic-information-mojobs',
        component: BasicInformationMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'basic-information-mojobs/:id',
        component: BasicInformationMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const basicInformationPopupRoute: Routes = [
    {
        path: 'basic-information-mojobs-new',
        component: BasicInformationMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'basic-information-mojobs/:id/edit',
        component: BasicInformationMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'basic-information-mojobs/:id/delete',
        component: BasicInformationMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
