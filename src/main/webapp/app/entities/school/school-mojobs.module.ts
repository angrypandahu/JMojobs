import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    SchoolMojobsService,
    SchoolMojobsPopupService,
    SchoolMojobsComponent,
    SchoolMojobsDetailComponent,
    SchoolMojobsDialogComponent,
    SchoolMojobsPopupComponent,
    SchoolMojobsDeletePopupComponent,
    SchoolMojobsDeleteDialogComponent,
    schoolRoute,
    schoolPopupRoute,
    SchoolMojobsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...schoolRoute,
    ...schoolPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SchoolMojobsComponent,
        SchoolMojobsDetailComponent,
        SchoolMojobsDialogComponent,
        SchoolMojobsDeleteDialogComponent,
        SchoolMojobsPopupComponent,
        SchoolMojobsDeletePopupComponent,
    ],
    entryComponents: [
        SchoolMojobsComponent,
        SchoolMojobsDialogComponent,
        SchoolMojobsPopupComponent,
        SchoolMojobsDeleteDialogComponent,
        SchoolMojobsDeletePopupComponent,
    ],
    providers: [
        SchoolMojobsService,
        SchoolMojobsPopupService,
        SchoolMojobsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsSchoolMojobsModule {}
