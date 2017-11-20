import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    ExperienceMojobsService,
    ExperienceMojobsPopupService,
    ExperienceMojobsComponent,
    ExperienceMojobsDetailComponent,
    ExperienceMojobsDialogComponent,
    ExperienceMojobsPopupComponent,
    ExperienceMojobsDeletePopupComponent,
    ExperienceMojobsDeleteDialogComponent,
    experienceRoute,
    experiencePopupRoute,
} from './';

const ENTITY_STATES = [
    ...experienceRoute,
    ...experiencePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ExperienceMojobsComponent,
        ExperienceMojobsDetailComponent,
        ExperienceMojobsDialogComponent,
        ExperienceMojobsDeleteDialogComponent,
        ExperienceMojobsPopupComponent,
        ExperienceMojobsDeletePopupComponent,
    ],
    entryComponents: [
        ExperienceMojobsComponent,
        ExperienceMojobsDialogComponent,
        ExperienceMojobsPopupComponent,
        ExperienceMojobsDeleteDialogComponent,
        ExperienceMojobsDeletePopupComponent,
    ],
    providers: [
        ExperienceMojobsService,
        ExperienceMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsExperienceMojobsModule {}
