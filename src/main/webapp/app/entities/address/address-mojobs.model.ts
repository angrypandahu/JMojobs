import { BaseEntity } from './../../shared';

export class AddressMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public mobile?: string,
        public email?: string,
        public phone?: string,
        public line?: string,
        public provinceId?: number,
        public cityId?: number,
        public townId?: number,
    ) {
    }
}
