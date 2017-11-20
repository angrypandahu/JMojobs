import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MjobMojobsComponent } from './mjob-mojobs.component';
import { MjobMojobsDetailComponent } from './mjob-mojobs-detail.component';
import { MjobMojobsPopupComponent } from './mjob-mojobs-dialog.component';
import { MjobMojobsDeletePopupComponent } from './mjob-mojobs-delete-dialog.component';

@Injectable()
export class MjobMojobsResolvePagingParams implements Resolve<any> {

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
        path: 'mjob-mojobs',
        component: MjobMojobsComponent,
        resolve: {
            'pagingParams': MjobMojobsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'mjob-mojobs/:id',
        component: MjobMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mjobPopupRoute: Routes = [
    {
        path: 'mjob-mojobs-new',
        component: MjobMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mjob-mojobs/:id/edit',
        component: MjobMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mjob-mojobs/:id/delete',
        component: MjobMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mjobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
