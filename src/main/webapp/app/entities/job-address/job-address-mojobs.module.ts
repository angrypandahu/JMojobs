import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    JobAddressMojobsService,
    JobAddressMojobsPopupService,
    JobAddressMojobsComponent,
    JobAddressMojobsDetailComponent,
    JobAddressMojobsDialogComponent,
    JobAddressMojobsPopupComponent,
    JobAddressMojobsDeletePopupComponent,
    JobAddressMojobsDeleteDialogComponent,
    jobAddressRoute,
    jobAddressPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobAddressRoute,
    ...jobAddressPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobAddressMojobsComponent,
        JobAddressMojobsDetailComponent,
        JobAddressMojobsDialogComponent,
        JobAddressMojobsDeleteDialogComponent,
        JobAddressMojobsPopupComponent,
        JobAddressMojobsDeletePopupComponent,
    ],
    entryComponents: [
        JobAddressMojobsComponent,
        JobAddressMojobsDialogComponent,
        JobAddressMojobsPopupComponent,
        JobAddressMojobsDeleteDialogComponent,
        JobAddressMojobsDeletePopupComponent,
    ],
    providers: [
        JobAddressMojobsService,
        JobAddressMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsJobAddressMojobsModule {}
