import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EducationBackgroundComponent } from './education-background.component';
import { EducationBackgroundDetailComponent } from './education-background-detail.component';
import { EducationBackgroundPopupComponent } from './education-background-dialog.component';
import { EducationBackgroundDeletePopupComponent } from './education-background-delete-dialog.component';

export const educationBackgroundRoute: Routes = [
    {
        path: 'education-background',
        component: EducationBackgroundComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'education-background/:id',
        component: EducationBackgroundDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const educationBackgroundPopupRoute: Routes = [
    {
        path: 'education-background-new',
        component: EducationBackgroundPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'education-background/:id/edit',
        component: EducationBackgroundPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'education-background/:id/delete',
        component: EducationBackgroundDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'EducationBackgrounds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
