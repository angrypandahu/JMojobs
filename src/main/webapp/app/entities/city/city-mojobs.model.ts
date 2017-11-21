import { BaseEntity } from './../../shared';

export class CityMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public provinceId?: number,
    ) {
    }
}
