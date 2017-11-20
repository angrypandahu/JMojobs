import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProfessionalDevelopmentMojobsComponent } from './professional-development-mojobs.component';
import { ProfessionalDevelopmentMojobsDetailComponent } from './professional-development-mojobs-detail.component';
import { ProfessionalDevelopmentMojobsPopupComponent } from './professional-development-mojobs-dialog.component';
import {
    ProfessionalDevelopmentMojobsDeletePopupComponent
} from './professional-development-mojobs-delete-dialog.component';

export const professionalDevelopmentRoute: Routes = [
    {
        path: 'professional-development-mojobs',
        component: ProfessionalDevelopmentMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'professional-development-mojobs/:id',
        component: ProfessionalDevelopmentMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const professionalDevelopmentPopupRoute: Routes = [
    {
        path: 'professional-development-mojobs-new',
        component: ProfessionalDevelopmentMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'professional-development-mojobs/:id/edit',
        component: ProfessionalDevelopmentMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'professional-development-mojobs/:id/delete',
        component: ProfessionalDevelopmentMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfessionalDevelopments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
