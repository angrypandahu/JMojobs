import { BaseEntity } from './../../shared';

export const enum JobType {
    'FACULTY',
    'PRINCIPAL',
    'SUPPORT_STAFF'
}

export class MjobMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public jobDescription?: any,
        public type?: JobType,
        public schoolId?: number,
        public subTypeId?: number,
    ) {
    }
}
