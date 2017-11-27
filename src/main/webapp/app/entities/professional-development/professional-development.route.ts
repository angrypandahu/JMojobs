import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProfessionalDevelopmentComponent } from './professional-development.component';
import { ProfessionalDevelopmentDetailComponent } from './professional-development-detail.component';
import { ProfessionalDevelopmentPopupComponent } from './professional-development-dialog.component';
import { ProfessionalDevelopmentDeletePopupComponent } from './professional-development-delete-dialog.component';

export const professionalDevelopmentRoute: Routes = [
    {
        path: 'professional-development',
        component: ProfessionalDevelopmentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'professional-development/:id',
        component: ProfessionalDevelopmentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const professionalDevelopmentPopupRoute: Routes = [
    {
        path: 'professional-development-new',
        component: ProfessionalDevelopmentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'professional-development/:id/edit',
        component: ProfessionalDevelopmentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'professional-development/:id/delete',
        component: ProfessionalDevelopmentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
