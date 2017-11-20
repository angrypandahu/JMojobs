import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import { JmojobsAdminModule } from '../../admin/admin.module';
import {
    ResumeMojobsService,
    ResumeMojobsPopupService,
    ResumeMojobsComponent,
    ResumeMojobsDetailComponent,
    ResumeMojobsDialogComponent,
    ResumeMojobsPopupComponent,
    ResumeMojobsDeletePopupComponent,
    ResumeMojobsDeleteDialogComponent,
    resumeRoute,
    resumePopupRoute,
} from './';

const ENTITY_STATES = [
    ...resumeRoute,
    ...resumePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        JmojobsAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ResumeMojobsComponent,
        ResumeMojobsDetailComponent,
        ResumeMojobsDialogComponent,
        ResumeMojobsDeleteDialogComponent,
        ResumeMojobsPopupComponent,
        ResumeMojobsDeletePopupComponent,
    ],
    entryComponents: [
        ResumeMojobsComponent,
        ResumeMojobsDialogComponent,
        ResumeMojobsPopupComponent,
        ResumeMojobsDeleteDialogComponent,
        ResumeMojobsDeletePopupComponent,
    ],
    providers: [
        ResumeMojobsService,
        ResumeMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsResumeMojobsModule {}
