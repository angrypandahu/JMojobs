import { BaseEntity } from './../../shared';

export class InvitationMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public subject?: string,
        public fromDate?: any,
        public content?: string,
        public schoolId?: number,
        public userId?: number,
    ) {
    }
}
