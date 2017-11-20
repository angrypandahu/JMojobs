import { BaseEntity } from './../../shared';

export class ChatMessageMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public sendTime?: any,
        public content?: string,
        public fromUserId?: number,
        public toUserId?: number,
    ) {
    }
}
