import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    JobSubTypeService,
    JobSubTypePopupService,
    JobSubTypeComponent,
    JobSubTypeDetailComponent,
    JobSubTypeDialogComponent,
    JobSubTypePopupComponent,
    JobSubTypeDeletePopupComponent,
    JobSubTypeDeleteDialogComponent,
    jobSubTypeRoute,
    jobSubTypePopupRoute,
    JobSubTypeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...jobSubTypeRoute,
    ...jobSubTypePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobSubTypeComponent,
        JobSubTypeDetailComponent,
        JobSubTypeDialogComponent,
        JobSubTypeDeleteDialogComponent,
        JobSubTypePopupComponent,
        JobSubTypeDeletePopupComponent,
    ],
    entryComponents: [
        JobSubTypeComponent,
        JobSubTypeDialogComponent,
        JobSubTypePopupComponent,
        JobSubTypeDeleteDialogComponent,
        JobSubTypeDeletePopupComponent,
    ],
    providers: [
        JobSubTypeService,
        JobSubTypePopupService,
        JobSubTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsJobSubTypeModule {}
