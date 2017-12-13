import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import { JmojobsAdminModule } from '../../admin/admin.module';
import {
    ResumeService,
    ResumePopupService,
    ResumeComponent,
    ResumeDetailComponent,
    ResumeDialogComponent,
    ResumePopupComponent,
    ResumeDeletePopupComponent,
    ResumeDeleteDialogComponent,
    resumeRoute,
    resumePopupRoute,
    ResumeResolvePagingParams,
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
        ResumeComponent,
        ResumeDetailComponent,
        ResumeDialogComponent,
        ResumeDeleteDialogComponent,
        ResumePopupComponent,
        ResumeDeletePopupComponent,
    ],
    entryComponents: [
        ResumeComponent,
        ResumeDialogComponent,
        ResumePopupComponent,
        ResumeDeleteDialogComponent,
        ResumeDeletePopupComponent,
    ],
    providers: [
        ResumeService,
        ResumePopupService,
        ResumeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsResumeModule {}
