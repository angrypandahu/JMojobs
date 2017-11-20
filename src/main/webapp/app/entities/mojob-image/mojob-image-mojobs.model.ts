import { BaseEntity } from './../../shared';

export class MojobImageMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public contentContentType?: string,
        public content?: any,
    ) {
    }
}
