import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    ProfessionalDevelopmentService,
    ProfessionalDevelopmentPopupService,
    ProfessionalDevelopmentComponent,
    ProfessionalDevelopmentDetailComponent,
    ProfessionalDevelopmentDialogComponent,
    ProfessionalDevelopmentPopupComponent,
    ProfessionalDevelopmentDeletePopupComponent,
    ProfessionalDevelopmentDeleteDialogComponent,
    professionalDevelopmentRoute,
    professionalDevelopmentPopupRoute,
} from './';

const ENTITY_STATES = [
    ...professionalDevelopmentRoute,
    ...professionalDevelopmentPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProfessionalDevelopmentComponent,
        ProfessionalDevelopmentDetailComponent,
        ProfessionalDevelopmentDialogComponent,
        ProfessionalDevelopmentDeleteDialogComponent,
        ProfessionalDevelopmentPopupComponent,
        ProfessionalDevelopmentDeletePopupComponent,
    ],
    entryComponents: [
        ProfessionalDevelopmentComponent,
        ProfessionalDevelopmentDialogComponent,
        ProfessionalDevelopmentPopupComponent,
        ProfessionalDevelopmentDeleteDialogComponent,
        ProfessionalDevelopmentDeletePopupComponent,
    ],
    providers: [
        ProfessionalDevelopmentService,
        ProfessionalDevelopmentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsProfessionalDevelopmentModule {}
