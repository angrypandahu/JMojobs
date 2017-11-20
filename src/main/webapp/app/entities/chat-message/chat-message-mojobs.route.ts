import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ChatMessageMojobsComponent } from './chat-message-mojobs.component';
import { ChatMessageMojobsDetailComponent } from './chat-message-mojobs-detail.component';
import { ChatMessageMojobsPopupComponent } from './chat-message-mojobs-dialog.component';
import { ChatMessageMojobsDeletePopupComponent } from './chat-message-mojobs-delete-dialog.component';

export const chatMessageRoute: Routes = [
    {
        path: 'chat-message-mojobs',
        component: ChatMessageMojobsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ChatMessages'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chat-message-mojobs/:id',
        component: ChatMessageMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ChatMessages'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const chatMessagePopupRoute: Routes = [
    {
        path: 'chat-message-mojobs-new',
        component: ChatMessageMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ChatMessages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chat-message-mojobs/:id/edit',
        component: ChatMessageMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ChatMessages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chat-message-mojobs/:id/delete',
        component: ChatMessageMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ChatMessages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
