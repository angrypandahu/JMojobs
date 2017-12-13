import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ExperienceComponent } from './experience.component';
import { ExperienceDetailComponent } from './experience-detail.component';
import { ExperiencePopupComponent } from './experience-dialog.component';
import { ExperienceDeletePopupComponent } from './experience-delete-dialog.component';

@Injectable()
export class ExperienceResolvePagingParams implements Resolve<any> {

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

export const experienceRoute: Routes = [
    {
        path: 'experience',
        component: ExperienceComponent,
        resolve: {
            'pagingParams': ExperienceResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'experience/:id',
        component: ExperienceDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const experiencePopupRoute: Routes = [
    {
        path: 'experience-new',
        component: ExperiencePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'experience/:id/edit',
        component: ExperiencePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'experience/:id/delete',
        component: ExperienceDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
