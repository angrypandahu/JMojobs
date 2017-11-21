import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    CityMojobsService,
    CityMojobsPopupService,
    CityMojobsComponent,
    CityMojobsDetailComponent,
    CityMojobsDialogComponent,
    CityMojobsPopupComponent,
    CityMojobsDeletePopupComponent,
    CityMojobsDeleteDialogComponent,
    cityRoute,
    cityPopupRoute,
} from './';

const ENTITY_STATES = [
    ...cityRoute,
    ...cityPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CityMojobsComponent,
        CityMojobsDetailComponent,
        CityMojobsDialogComponent,
        CityMojobsDeleteDialogComponent,
        CityMojobsPopupComponent,
        CityMojobsDeletePopupComponent,
    ],
    entryComponents: [
        CityMojobsComponent,
        CityMojobsDialogComponent,
        CityMojobsPopupComponent,
        CityMojobsDeleteDialogComponent,
        CityMojobsDeletePopupComponent,
    ],
    providers: [
        CityMojobsService,
        CityMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsCityMojobsModule {}
