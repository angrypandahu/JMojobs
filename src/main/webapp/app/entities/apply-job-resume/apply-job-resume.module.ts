import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    ApplyJobResumeService,
    ApplyJobResumePopupService,
    ApplyJobResumeComponent,
    ApplyJobResumeDetailComponent,
    ApplyJobResumeDialogComponent,
    ApplyJobResumePopupComponent,
    ApplyJobResumeDeletePopupComponent,
    ApplyJobResumeDeleteDialogComponent,
    applyJobResumeRoute,
    applyJobResumePopupRoute,
    ApplyJobResumeResolvePagingParams,
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
        ApplyJobResumeComponent,
        ApplyJobResumeDetailComponent,
        ApplyJobResumeDialogComponent,
        ApplyJobResumeDeleteDialogComponent,
        ApplyJobResumePopupComponent,
        ApplyJobResumeDeletePopupComponent,
    ],
    entryComponents: [
        ApplyJobResumeComponent,
        ApplyJobResumeDialogComponent,
        ApplyJobResumePopupComponent,
        ApplyJobResumeDeleteDialogComponent,
        ApplyJobResumeDeletePopupComponent,
    ],
    providers: [
        ApplyJobResumeService,
        ApplyJobResumePopupService,
        ApplyJobResumeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsApplyJobResumeModule {}
