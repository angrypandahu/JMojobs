import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    EducationBackgroundMojobsService,
    EducationBackgroundMojobsPopupService,
    EducationBackgroundMojobsComponent,
    EducationBackgroundMojobsDetailComponent,
    EducationBackgroundMojobsDialogComponent,
    EducationBackgroundMojobsPopupComponent,
    EducationBackgroundMojobsDeletePopupComponent,
    EducationBackgroundMojobsDeleteDialogComponent,
    educationBackgroundRoute,
    educationBackgroundPopupRoute,
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
        EducationBackgroundMojobsComponent,
        EducationBackgroundMojobsDetailComponent,
        EducationBackgroundMojobsDialogComponent,
        EducationBackgroundMojobsDeleteDialogComponent,
        EducationBackgroundMojobsPopupComponent,
        EducationBackgroundMojobsDeletePopupComponent,
    ],
    entryComponents: [
        EducationBackgroundMojobsComponent,
        EducationBackgroundMojobsDialogComponent,
        EducationBackgroundMojobsPopupComponent,
        EducationBackgroundMojobsDeleteDialogComponent,
        EducationBackgroundMojobsDeletePopupComponent,
    ],
    providers: [
        EducationBackgroundMojobsService,
        EducationBackgroundMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsEducationBackgroundMojobsModule {}
