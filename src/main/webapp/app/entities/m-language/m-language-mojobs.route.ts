import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MLanguageMojobsComponent } from './m-language-mojobs.component';
import { MLanguageMojobsDetailComponent } from './m-language-mojobs-detail.component';
import { MLanguageMojobsPopupComponent } from './m-language-mojobs-dialog.component';
import { MLanguageMojobsDeletePopupComponent } from './m-language-mojobs-delete-dialog.component';

export const mLanguageRoute: Routes = [
    {
        path: 'm-language-mojobs',
        component: MLanguageMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'm-language-mojobs/:id',
        component: MLanguageMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mLanguagePopupRoute: Routes = [
    {
        path: 'm-language-mojobs-new',
        component: MLanguageMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'm-language-mojobs/:id/edit',
        component: MLanguageMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'm-language-mojobs/:id/delete',
        component: MLanguageMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MLanguages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
