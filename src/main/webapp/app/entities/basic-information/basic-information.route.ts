import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BasicInformationComponent } from './basic-information.component';
import { BasicInformationDetailComponent } from './basic-information-detail.component';
import { BasicInformationPopupComponent } from './basic-information-dialog.component';
import { BasicInformationDeletePopupComponent } from './basic-information-delete-dialog.component';

@Injectable()
export class BasicInformationResolvePagingParams implements Resolve<any> {

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

export const basicInformationRoute: Routes = [
    {
        path: 'basic-information',
        component: BasicInformationComponent,
        resolve: {
            'pagingParams': BasicInformationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'basic-information/:id',
        component: BasicInformationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const basicInformationPopupRoute: Routes = [
    {
        path: 'basic-information-new',
        component: BasicInformationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'basic-information/:id/edit',
        component: BasicInformationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'basic-information/:id/delete',
        component: BasicInformationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BasicInformations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
