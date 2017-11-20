import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    ProfessionalDevelopmentMojobsService,
    ProfessionalDevelopmentMojobsPopupService,
    ProfessionalDevelopmentMojobsComponent,
    ProfessionalDevelopmentMojobsDetailComponent,
    ProfessionalDevelopmentMojobsDialogComponent,
    ProfessionalDevelopmentMojobsPopupComponent,
    ProfessionalDevelopmentMojobsDeletePopupComponent,
    ProfessionalDevelopmentMojobsDeleteDialogComponent,
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
        ProfessionalDevelopmentMojobsComponent,
        ProfessionalDevelopmentMojobsDetailComponent,
        ProfessionalDevelopmentMojobsDialogComponent,
        ProfessionalDevelopmentMojobsDeleteDialogComponent,
        ProfessionalDevelopmentMojobsPopupComponent,
        ProfessionalDevelopmentMojobsDeletePopupComponent,
    ],
    entryComponents: [
        ProfessionalDevelopmentMojobsComponent,
        ProfessionalDevelopmentMojobsDialogComponent,
        ProfessionalDevelopmentMojobsPopupComponent,
        ProfessionalDevelopmentMojobsDeleteDialogComponent,
        ProfessionalDevelopmentMojobsDeletePopupComponent,
    ],
    providers: [
        ProfessionalDevelopmentMojobsService,
        ProfessionalDevelopmentMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsProfessionalDevelopmentMojobsModule {}
