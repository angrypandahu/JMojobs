import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    JobSubTypeMojobsService,
    JobSubTypeMojobsPopupService,
    JobSubTypeMojobsComponent,
    JobSubTypeMojobsDetailComponent,
    JobSubTypeMojobsDialogComponent,
    JobSubTypeMojobsPopupComponent,
    JobSubTypeMojobsDeletePopupComponent,
    JobSubTypeMojobsDeleteDialogComponent,
    jobSubTypeRoute,
    jobSubTypePopupRoute,
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
        JobSubTypeMojobsComponent,
        JobSubTypeMojobsDetailComponent,
        JobSubTypeMojobsDialogComponent,
        JobSubTypeMojobsDeleteDialogComponent,
        JobSubTypeMojobsPopupComponent,
        JobSubTypeMojobsDeletePopupComponent,
    ],
    entryComponents: [
        JobSubTypeMojobsComponent,
        JobSubTypeMojobsDialogComponent,
        JobSubTypeMojobsPopupComponent,
        JobSubTypeMojobsDeleteDialogComponent,
        JobSubTypeMojobsDeletePopupComponent,
    ],
    providers: [
        JobSubTypeMojobsService,
        JobSubTypeMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsJobSubTypeMojobsModule {}
