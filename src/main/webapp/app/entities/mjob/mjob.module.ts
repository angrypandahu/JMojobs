import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    MjobService,
    MjobPopupService,
    MjobComponent,
    MjobDetailComponent,
    MjobDialogComponent,
    MjobPopupComponent,
    MjobDeletePopupComponent,
    MjobDeleteDialogComponent,
    mjobRoute,
    mjobPopupRoute,
    MjobResolvePagingParams,
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
        MjobComponent,
        MjobDetailComponent,
        MjobDialogComponent,
        MjobDeleteDialogComponent,
        MjobPopupComponent,
        MjobDeletePopupComponent,
    ],
    entryComponents: [
        MjobComponent,
        MjobDialogComponent,
        MjobPopupComponent,
        MjobDeleteDialogComponent,
        MjobDeletePopupComponent,
    ],
    providers: [
        MjobService,
        MjobPopupService,
        MjobResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsMjobModule {}
