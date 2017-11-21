import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    MojobImageMojobsService,
    MojobImageMojobsPopupService,
    MojobImageMojobsComponent,
    MojobImageMojobsDetailComponent,
    MojobImageMojobsDialogComponent,
    MojobImageMojobsPopupComponent,
    MojobImageMojobsDeletePopupComponent,
    MojobImageMojobsDeleteDialogComponent,
    mojobImageRoute,
    mojobImagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...mojobImageRoute,
    ...mojobImagePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MojobImageMojobsComponent,
        MojobImageMojobsDetailComponent,
        MojobImageMojobsDialogComponent,
        MojobImageMojobsDeleteDialogComponent,
        MojobImageMojobsPopupComponent,
        MojobImageMojobsDeletePopupComponent,
    ],
    entryComponents: [
        MojobImageMojobsComponent,
        MojobImageMojobsDialogComponent,
        MojobImageMojobsPopupComponent,
        MojobImageMojobsDeleteDialogComponent,
        MojobImageMojobsDeletePopupComponent,
    ],
    providers: [
        MojobImageMojobsService,
        MojobImageMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsMojobImageMojobsModule {}
