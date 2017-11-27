import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MjobComponent } from './mjob.component';
import { MjobDetailComponent } from './mjob-detail.component';
import { MjobPopupComponent } from './mjob-dialog.component';
import { MjobDeletePopupComponent } from './mjob-delete-dialog.component';

@Injectable()
export class MjobResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const mjobRoute: Routes = [
    {
        path: 'mjob',
        component: MjobComponent,
        resolve: {
            'pagingParams': MjobResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'mjob/:id',
        component: MjobDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mjobPopupRoute: Routes = [
    {
        path: 'mjob-new',
        component: MjobPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mjob/:id/edit',
        component: MjobPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mjob/:id/delete',
        component: MjobDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
