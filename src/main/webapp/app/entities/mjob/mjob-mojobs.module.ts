import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    MjobMojobsService,
    MjobMojobsPopupService,
    MjobMojobsComponent,
    MjobMojobsDetailComponent,
    MjobMojobsDialogComponent,
    MjobMojobsPopupComponent,
    MjobMojobsDeletePopupComponent,
    MjobMojobsDeleteDialogComponent,
    mjobRoute,
    mjobPopupRoute,
    MjobMojobsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...mjobRoute,
    ...mjobPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MjobMojobsComponent,
        MjobMojobsDetailComponent,
        MjobMojobsDialogComponent,
        MjobMojobsDeleteDialogComponent,
        MjobMojobsPopupComponent,
        MjobMojobsDeletePopupComponent,
    ],
    entryComponents: [
        MjobMojobsComponent,
        MjobMojobsDialogComponent,
        MjobMojobsPopupComponent,
        MjobMojobsDeleteDialogComponent,
        MjobMojobsDeletePopupComponent,
    ],
    providers: [
        MjobMojobsService,
        MjobMojobsPopupService,
        MjobMojobsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsMjobMojobsModule {}
