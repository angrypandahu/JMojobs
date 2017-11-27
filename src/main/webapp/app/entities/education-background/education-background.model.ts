import { BaseEntity } from './../../shared';

export const enum EducationLevel {
    'ASSOCIATE',
    'BACHELOR',
    'MASTER',
    'DOCTORATE'
}

export class EducationBackground implements BaseEntity {
    constructor(
        public id?: number,
        public school?: string,
        public major?: string,
        public degree?: EducationLevel,
        public location?: string,
        public fromDate?: any,
        public toDate?: any,
        public resumeId?: number,
    ) {
    }
}
