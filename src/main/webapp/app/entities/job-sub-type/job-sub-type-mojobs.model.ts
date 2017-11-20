import { BaseEntity } from './../../shared';

export const enum JobType {
    'FACULTY',
    'PRINCIPAL',
    'SUPPORT_STAFF'
}

export class JobSubTypeMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public parent?: JobType,
        public name?: string,
    ) {
    }
}
