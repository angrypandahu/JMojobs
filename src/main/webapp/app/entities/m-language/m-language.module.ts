import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    MLanguageService,
    MLanguagePopupService,
    MLanguageComponent,
    MLanguageDetailComponent,
    MLanguageDialogComponent,
    MLanguagePopupComponent,
    MLanguageDeletePopupComponent,
    MLanguageDeleteDialogComponent,
    mLanguageRoute,
    mLanguagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...mLanguageRoute,
    ...mLanguagePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MLanguageComponent,
        MLanguageDetailComponent,
        MLanguageDialogComponent,
        MLanguageDeleteDialogComponent,
        MLanguagePopupComponent,
        MLanguageDeletePopupComponent,
    ],
    entryComponents: [
        MLanguageComponent,
        MLanguageDialogComponent,
        MLanguagePopupComponent,
        MLanguageDeleteDialogComponent,
        MLanguageDeletePopupComponent,
    ],
    providers: [
        MLanguageService,
        MLanguagePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsMLanguageModule {}
