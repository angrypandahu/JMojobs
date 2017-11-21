import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    TownMojobsService,
    TownMojobsPopupService,
    TownMojobsComponent,
    TownMojobsDetailComponent,
    TownMojobsDialogComponent,
    TownMojobsPopupComponent,
    TownMojobsDeletePopupComponent,
    TownMojobsDeleteDialogComponent,
    townRoute,
    townPopupRoute,
} from './';

const ENTITY_STATES = [
    ...townRoute,
    ...townPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TownMojobsComponent,
        TownMojobsDetailComponent,
        TownMojobsDialogComponent,
        TownMojobsDeleteDialogComponent,
        TownMojobsPopupComponent,
        TownMojobsDeletePopupComponent,
    ],
    entryComponents: [
        TownMojobsComponent,
        TownMojobsDialogComponent,
        TownMojobsPopupComponent,
        TownMojobsDeleteDialogComponent,
        TownMojobsDeletePopupComponent,
    ],
    providers: [
        TownMojobsService,
        TownMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsTownMojobsModule {}
