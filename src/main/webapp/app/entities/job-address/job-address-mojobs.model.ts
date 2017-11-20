import { BaseEntity } from './../../shared';

export class JobAddressMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public addressId?: number,
        public jobId?: number,
    ) {
    }
}
