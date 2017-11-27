import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MLanguageComponent } from './m-language.component';
import { MLanguageDetailComponent } from './m-language-detail.component';
import { MLanguagePopupComponent } from './m-language-dialog.component';
import { MLanguageDeletePopupComponent } from './m-language-delete-dialog.component';

export const mLanguageRoute: Routes = [
    {
        path: 'm-language',
        component: MLanguageComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'm-language/:id',
        component: MLanguageDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mLanguagePopupRoute: Routes = [
    {
        path: 'm-language-new',
        component: MLanguagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'm-language/:id/edit',
        component: MLanguagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'm-language/:id/delete',
        component: MLanguageDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
