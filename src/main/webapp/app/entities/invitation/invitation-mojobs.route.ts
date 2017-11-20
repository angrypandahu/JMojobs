import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InvitationMojobsComponent } from './invitation-mojobs.component';
import { InvitationMojobsDetailComponent } from './invitation-mojobs-detail.component';
import { InvitationMojobsPopupComponent } from './invitation-mojobs-dialog.component';
import { InvitationMojobsDeletePopupComponent } from './invitation-mojobs-delete-dialog.component';

export const invitationRoute: Routes = [
    {
        path: 'invitation-mojobs',
        component: InvitationMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'invitation-mojobs/:id',
        component: InvitationMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const invitationPopupRoute: Routes = [
    {
        path: 'invitation-mojobs-new',
        component: InvitationMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'invitation-mojobs/:id/edit',
        component: InvitationMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'invitation-mojobs/:id/delete',
        component: InvitationMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
