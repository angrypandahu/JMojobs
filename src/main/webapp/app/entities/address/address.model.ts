import { BaseEntity } from './../../shared';

export const enum Location {
    'PUXI',
    'PUDONG',
    'JIANGSU',
    'ZHEJIA'
}

export class Address implements BaseEntity {
    constructor(
        public id?: number,
        public name?: Location,
        public mobile?: string,
        public email?: string,
        public phone?: string,
        public line?: string,
    ) {
    }
}
