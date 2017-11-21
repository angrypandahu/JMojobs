import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    ApplyJobResumeMojobsService,
    ApplyJobResumeMojobsPopupService,
    ApplyJobResumeMojobsComponent,
    ApplyJobResumeMojobsDetailComponent,
    ApplyJobResumeMojobsDialogComponent,
    ApplyJobResumeMojobsPopupComponent,
    ApplyJobResumeMojobsDeletePopupComponent,
    ApplyJobResumeMojobsDeleteDialogComponent,
    applyJobResumeRoute,
    applyJobResumePopupRoute,
    ApplyJobResumeMojobsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...applyJobResumeRoute,
    ...applyJobResumePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ApplyJobResumeMojobsComponent,
        ApplyJobResumeMojobsDetailComponent,
        ApplyJobResumeMojobsDialogComponent,
        ApplyJobResumeMojobsDeleteDialogComponent,
        ApplyJobResumeMojobsPopupComponent,
        ApplyJobResumeMojobsDeletePopupComponent,
    ],
    entryComponents: [
        ApplyJobResumeMojobsComponent,
        ApplyJobResumeMojobsDialogComponent,
        ApplyJobResumeMojobsPopupComponent,
        ApplyJobResumeMojobsDeleteDialogComponent,
        ApplyJobResumeMojobsDeletePopupComponent,
    ],
    providers: [
        ApplyJobResumeMojobsService,
        ApplyJobResumeMojobsPopupService,
        ApplyJobResumeMojobsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsApplyJobResumeMojobsModule {}
