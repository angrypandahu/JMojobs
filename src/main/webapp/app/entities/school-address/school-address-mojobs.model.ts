import { BaseEntity } from './../../shared';

export class SchoolAddressMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public addressId?: number,
        public schoolId?: number,
    ) {
    }
}
