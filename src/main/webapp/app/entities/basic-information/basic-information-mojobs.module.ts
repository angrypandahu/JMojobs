import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    BasicInformationMojobsService,
    BasicInformationMojobsPopupService,
    BasicInformationMojobsComponent,
    BasicInformationMojobsDetailComponent,
    BasicInformationMojobsDialogComponent,
    BasicInformationMojobsPopupComponent,
    BasicInformationMojobsDeletePopupComponent,
    BasicInformationMojobsDeleteDialogComponent,
    basicInformationRoute,
    basicInformationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...basicInformationRoute,
    ...basicInformationPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BasicInformationMojobsComponent,
        BasicInformationMojobsDetailComponent,
        BasicInformationMojobsDialogComponent,
        BasicInformationMojobsDeleteDialogComponent,
        BasicInformationMojobsPopupComponent,
        BasicInformationMojobsDeletePopupComponent,
    ],
    entryComponents: [
        BasicInformationMojobsComponent,
        BasicInformationMojobsDialogComponent,
        BasicInformationMojobsPopupComponent,
        BasicInformationMojobsDeleteDialogComponent,
        BasicInformationMojobsDeletePopupComponent,
    ],
    providers: [
        BasicInformationMojobsService,
        BasicInformationMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsBasicInformationMojobsModule {}
