import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import { JmojobsAdminModule } from '../../admin/admin.module';
import {
    ChatMessageMojobsService,
    ChatMessageMojobsPopupService,
    ChatMessageMojobsComponent,
    ChatMessageMojobsDetailComponent,
    ChatMessageMojobsDialogComponent,
    ChatMessageMojobsPopupComponent,
    ChatMessageMojobsDeletePopupComponent,
    ChatMessageMojobsDeleteDialogComponent,
    chatMessageRoute,
    chatMessagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...chatMessageRoute,
    ...chatMessagePopupRoute,
];

@NgModule({
    imports: [
        JmojobsSharedModule,
        JmojobsAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ChatMessageMojobsComponent,
        ChatMessageMojobsDetailComponent,
        ChatMessageMojobsDialogComponent,
        ChatMessageMojobsDeleteDialogComponent,
        ChatMessageMojobsPopupComponent,
        ChatMessageMojobsDeletePopupComponent,
    ],
    entryComponents: [
        ChatMessageMojobsComponent,
        ChatMessageMojobsDialogComponent,
        ChatMessageMojobsPopupComponent,
        ChatMessageMojobsDeleteDialogComponent,
        ChatMessageMojobsDeletePopupComponent,
    ],
    providers: [
        ChatMessageMojobsService,
        ChatMessageMojobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsChatMessageMojobsModule {}
