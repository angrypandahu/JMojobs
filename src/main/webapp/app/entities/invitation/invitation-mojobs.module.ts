import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import { JmojobsAdminModule } from '../../admin/admin.module';
import {
    InvitationMojobsService,
    InvitationMojobsPopupService,
    InvitationMojobsComponent,
    InvitationMojobsDetailComponent,
    InvitationMojobsDialogComponent,
    InvitationMojobsPopupComponent,
    InvitationMojobsDeletePopupComponent,
    InvitationMojobsDeleteDialogComponent,
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
        InvitationMojobsComponent,
        InvitationMojobsDetailComponent,
        InvitationMojobsDialogComponent,
        InvitationMojobsDeleteDialogComponent,
        InvitationMojobsPopupComponent,
        InvitationMojobsDeletePopupComponent,
    ],
    entryComponents: [
        InvitationMojobsComponent,
        InvitationMojobsDialogComponent,
        InvitationMojobsPopupComponent,
        InvitationMojobsDeleteDialogComponent,
        InvitationMojobsDeletePopupComponent,
    ],
    providers: [
        InvitationMojobsService,
        InvitationMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsInvitationMojobsModule {}
