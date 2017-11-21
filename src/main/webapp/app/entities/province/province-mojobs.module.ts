import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    ProvinceMojobsService,
    ProvinceMojobsPopupService,
    ProvinceMojobsComponent,
    ProvinceMojobsDetailComponent,
    ProvinceMojobsDialogComponent,
    ProvinceMojobsPopupComponent,
    ProvinceMojobsDeletePopupComponent,
    ProvinceMojobsDeleteDialogComponent,
    provinceRoute,
    provincePopupRoute,
} from './';

const ENTITY_STATES = [
    ...provinceRoute,
    ...provincePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProvinceMojobsComponent,
        ProvinceMojobsDetailComponent,
        ProvinceMojobsDialogComponent,
        ProvinceMojobsDeleteDialogComponent,
        ProvinceMojobsPopupComponent,
        ProvinceMojobsDeletePopupComponent,
    ],
    entryComponents: [
        ProvinceMojobsComponent,
        ProvinceMojobsDialogComponent,
        ProvinceMojobsPopupComponent,
        ProvinceMojobsDeleteDialogComponent,
        ProvinceMojobsDeletePopupComponent,
    ],
    providers: [
        ProvinceMojobsService,
        ProvinceMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsProvinceMojobsModule {}
