import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    SchoolAddressMojobsService,
    SchoolAddressMojobsPopupService,
    SchoolAddressMojobsComponent,
    SchoolAddressMojobsDetailComponent,
    SchoolAddressMojobsDialogComponent,
    SchoolAddressMojobsPopupComponent,
    SchoolAddressMojobsDeletePopupComponent,
    SchoolAddressMojobsDeleteDialogComponent,
    schoolAddressRoute,
    schoolAddressPopupRoute,
} from './';

const ENTITY_STATES = [
    ...schoolAddressRoute,
    ...schoolAddressPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SchoolAddressMojobsComponent,
        SchoolAddressMojobsDetailComponent,
        SchoolAddressMojobsDialogComponent,
        SchoolAddressMojobsDeleteDialogComponent,
        SchoolAddressMojobsPopupComponent,
        SchoolAddressMojobsDeletePopupComponent,
    ],
    entryComponents: [
        SchoolAddressMojobsComponent,
        SchoolAddressMojobsDialogComponent,
        SchoolAddressMojobsPopupComponent,
        SchoolAddressMojobsDeleteDialogComponent,
        SchoolAddressMojobsDeletePopupComponent,
    ],
    providers: [
        SchoolAddressMojobsService,
        SchoolAddressMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsSchoolAddressMojobsModule {}
