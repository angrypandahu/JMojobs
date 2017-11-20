import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EducationBackgroundMojobsComponent } from './education-background-mojobs.component';
import { EducationBackgroundMojobsDetailComponent } from './education-background-mojobs-detail.component';
import { EducationBackgroundMojobsPopupComponent } from './education-background-mojobs-dialog.component';
import { EducationBackgroundMojobsDeletePopupComponent } from './education-background-mojobs-delete-dialog.component';

export const educationBackgroundRoute: Routes = [
    {
        path: 'education-background-mojobs',
        component: EducationBackgroundMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'education-background-mojobs/:id',
        component: EducationBackgroundMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const educationBackgroundPopupRoute: Routes = [
    {
        path: 'education-background-mojobs-new',
        component: EducationBackgroundMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'education-background-mojobs/:id/edit',
        component: EducationBackgroundMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'education-background-mojobs/:id/delete',
        component: EducationBackgroundMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
