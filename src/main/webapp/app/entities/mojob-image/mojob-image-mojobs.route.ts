import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MojobImageMojobsComponent } from './mojob-image-mojobs.component';
import { MojobImageMojobsDetailComponent } from './mojob-image-mojobs-detail.component';
import { MojobImageMojobsPopupComponent } from './mojob-image-mojobs-dialog.component';
import { MojobImageMojobsDeletePopupComponent } from './mojob-image-mojobs-delete-dialog.component';

export const mojobImageRoute: Routes = [
    {
        path: 'mojob-image-mojobs',
        component: MojobImageMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MojobImages'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'mojob-image-mojobs/:id',
        component: MojobImageMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MojobImages'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mojobImagePopupRoute: Routes = [
    {
        path: 'mojob-image-mojobs-new',
        component: MojobImageMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MojobImages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mojob-image-mojobs/:id/edit',
        component: MojobImageMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MojobImages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mojob-image-mojobs/:id/delete',
        component: MojobImageMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MojobImages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
