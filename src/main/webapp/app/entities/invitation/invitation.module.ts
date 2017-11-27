import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import { JmojobsAdminModule } from '../../admin/admin.module';
import {
    InvitationService,
    InvitationPopupService,
    InvitationComponent,
    InvitationDetailComponent,
    InvitationDialogComponent,
    InvitationPopupComponent,
    InvitationDeletePopupComponent,
    InvitationDeleteDialogComponent,
    invitationRoute,
    invitationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...invitationRoute,
    ...invitationPopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        JmojobsAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        InvitationComponent,
        InvitationDetailComponent,
        InvitationDialogComponent,
        InvitationDeleteDialogComponent,
        InvitationPopupComponent,
        InvitationDeletePopupComponent,
    ],
    entryComponents: [
        InvitationComponent,
        InvitationDialogComponent,
        InvitationPopupComponent,
        InvitationDeleteDialogComponent,
        InvitationDeletePopupComponent,
    ],
    providers: [
        InvitationService,
        InvitationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsInvitationModule {}
