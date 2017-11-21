import { BaseEntity } from './../../shared';

export class ProvinceMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}
