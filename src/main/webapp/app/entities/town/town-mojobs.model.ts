import { BaseEntity } from './../../shared';

export class TownMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public zip?: string,
        public cityId?: number,
    ) {
    }
}
