import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmojobsSharedModule } from '../../shared';
import { JmojobsAdminModule } from '../../admin/admin.module';
import {
    ChatMessageService,
    ChatMessagePopupService,
    ChatMessageComponent,
    ChatMessageDetailComponent,
    ChatMessageDialogComponent,
    ChatMessagePopupComponent,
    ChatMessageDeletePopupComponent,
    ChatMessageDeleteDialogComponent,
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
        ChatMessageComponent,
        ChatMessageDetailComponent,
        ChatMessageDialogComponent,
        ChatMessageDeleteDialogComponent,
        ChatMessagePopupComponent,
        ChatMessageDeletePopupComponent,
    ],
    entryComponents: [
        ChatMessageComponent,
        ChatMessageDialogComponent,
        ChatMessagePopupComponent,
        ChatMessageDeleteDialogComponent,
        ChatMessageDeletePopupComponent,
    ],
    providers: [
        ChatMessageService,
        ChatMessagePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsChatMessageModule {}
