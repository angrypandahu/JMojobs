import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import {
    AddressMojobsService,
    AddressMojobsPopupService,
    AddressMojobsComponent,
    AddressMojobsDetailComponent,
    AddressMojobsDialogComponent,
    AddressMojobsPopupComponent,
    AddressMojobsDeletePopupComponent,
    AddressMojobsDeleteDialogComponent,
    addressRoute,
    addressPopupRoute,
} from './';

const ENTITY_STATES = [
    ...addressRoute,
    ...addressPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AddressMojobsComponent,
        AddressMojobsDetailComponent,
        AddressMojobsDialogComponent,
        AddressMojobsDeleteDialogComponent,
        AddressMojobsPopupComponent,
        AddressMojobsDeletePopupComponent,
    ],
    entryComponents: [
        AddressMojobsComponent,
        AddressMojobsDialogComponent,
        AddressMojobsPopupComponent,
        AddressMojobsDeleteDialogComponent,
        AddressMojobsDeletePopupComponent,
    ],
    providers: [
        AddressMojobsService,
        AddressMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsAddressMojobsModule {}
