import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ExperienceMojobsComponent } from './experience-mojobs.component';
import { ExperienceMojobsDetailComponent } from './experience-mojobs-detail.component';
import { ExperienceMojobsPopupComponent } from './experience-mojobs-dialog.component';
import { ExperienceMojobsDeletePopupComponent } from './experience-mojobs-delete-dialog.component';

export const experienceRoute: Routes = [
    {
        path: 'experience-mojobs',
        component: ExperienceMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'experience-mojobs/:id',
        component: ExperienceMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const experiencePopupRoute: Routes = [
    {
        path: 'experience-mojobs-new',
        component: ExperienceMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'experience-mojobs/:id/edit',
        component: ExperienceMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'experience-mojobs/:id/delete',
        component: ExperienceMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
