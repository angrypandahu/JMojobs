import { BaseEntity } from './../../shared';

export const enum SchoolLevel {
    'KINDERGARTEN',
    'ELEMENTARY',
    'MIDDLE_SCHOOL',
    'HIGH_SCHOOL',
    'UNIVERSITY'
}

export const enum SchoolType {
    'INTERNATIONAL_BILINGUAL_SCHOOL',
    'LANGUAGE_TRAINING'
}

export class SchoolMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public level?: SchoolLevel,
        public schoolType?: SchoolType,
        public logoId?: number,
        public jobs?: BaseEntity[],
    ) {
    }
}
