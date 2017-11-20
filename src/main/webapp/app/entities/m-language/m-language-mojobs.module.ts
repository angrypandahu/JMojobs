import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    MLanguageMojobsService,
    MLanguageMojobsPopupService,
    MLanguageMojobsComponent,
    MLanguageMojobsDetailComponent,
    MLanguageMojobsDialogComponent,
    MLanguageMojobsPopupComponent,
    MLanguageMojobsDeletePopupComponent,
    MLanguageMojobsDeleteDialogComponent,
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
        MLanguageMojobsComponent,
        MLanguageMojobsDetailComponent,
        MLanguageMojobsDialogComponent,
        MLanguageMojobsDeleteDialogComponent,
        MLanguageMojobsPopupComponent,
        MLanguageMojobsDeletePopupComponent,
    ],
    entryComponents: [
        MLanguageMojobsComponent,
        MLanguageMojobsDialogComponent,
        MLanguageMojobsPopupComponent,
        MLanguageMojobsDeleteDialogComponent,
        MLanguageMojobsDeletePopupComponent,
    ],
    providers: [
        MLanguageMojobsService,
        MLanguageMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsMLanguageMojobsModule {}
