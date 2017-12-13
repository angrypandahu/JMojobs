import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    EducationBackgroundService,
    EducationBackgroundPopupService,
    EducationBackgroundComponent,
    EducationBackgroundDetailComponent,
    EducationBackgroundDialogComponent,
    EducationBackgroundPopupComponent,
    EducationBackgroundDeletePopupComponent,
    EducationBackgroundDeleteDialogComponent,
    educationBackgroundRoute,
    educationBackgroundPopupRoute,
    EducationBackgroundResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...educationBackgroundRoute,
    ...educationBackgroundPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EducationBackgroundComponent,
        EducationBackgroundDetailComponent,
        EducationBackgroundDialogComponent,
        EducationBackgroundDeleteDialogComponent,
        EducationBackgroundPopupComponent,
        EducationBackgroundDeletePopupComponent,
    ],
    entryComponents: [
        EducationBackgroundComponent,
        EducationBackgroundDialogComponent,
        EducationBackgroundPopupComponent,
        EducationBackgroundDeleteDialogComponent,
        EducationBackgroundDeletePopupComponent,
    ],
    providers: [
        EducationBackgroundService,
        EducationBackgroundPopupService,
        EducationBackgroundResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsEducationBackgroundModule {}
