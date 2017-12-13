import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    BasicInformationService,
    BasicInformationPopupService,
    BasicInformationComponent,
    BasicInformationDetailComponent,
    BasicInformationDialogComponent,
    BasicInformationPopupComponent,
    BasicInformationDeletePopupComponent,
    BasicInformationDeleteDialogComponent,
    basicInformationRoute,
    basicInformationPopupRoute,
    BasicInformationResolvePagingParams,
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
        BasicInformationComponent,
        BasicInformationDetailComponent,
        BasicInformationDialogComponent,
        BasicInformationDeleteDialogComponent,
        BasicInformationPopupComponent,
        BasicInformationDeletePopupComponent,
    ],
    entryComponents: [
        BasicInformationComponent,
        BasicInformationDialogComponent,
        BasicInformationPopupComponent,
        BasicInformationDeleteDialogComponent,
        BasicInformationDeletePopupComponent,
    ],
    providers: [
        BasicInformationService,
        BasicInformationPopupService,
        BasicInformationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsBasicInformationModule {}
