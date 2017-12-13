import { BaseEntity } from './../../shared';

export class Image implements BaseEntity {
    constructor(
        public id?: number,
        public imgContentType?: string,
        public img?: any,
    ) {
    }
}
