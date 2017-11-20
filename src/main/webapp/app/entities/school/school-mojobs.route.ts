import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SchoolMojobsComponent } from './school-mojobs.component';
import { SchoolMojobsDetailComponent } from './school-mojobs-detail.component';
import { SchoolMojobsPopupComponent } from './school-mojobs-dialog.component';
import { SchoolMojobsDeletePopupComponent } from './school-mojobs-delete-dialog.component';

@Injectable()
export class SchoolMojobsResolvePagingParams implements Resolve<any> {

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

export const schoolRoute: Routes = [
    {
        path: 'school-mojobs',
        component: SchoolMojobsComponent,
        resolve: {
            'pagingParams': SchoolMojobsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'school-mojobs/:id',
        component: SchoolMojobsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const schoolPopupRoute: Routes = [
    {
        path: 'school-mojobs-new',
        component: SchoolMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'school-mojobs/:id/edit',
        component: SchoolMojobsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'school-mojobs/:id/delete',
        component: SchoolMojobsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
