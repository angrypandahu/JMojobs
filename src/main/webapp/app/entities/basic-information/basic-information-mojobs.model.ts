import { BaseEntity } from './../../shared';

export const enum Gender {
    'MALE',
    'FEMALE'
}

export const enum EducationLevel {
    'ASSOCIATE',
    'BACHELOR',
    'MASTER',
    'DOCTORATE'
}

export class BasicInformationMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public lastName?: string,
        public firstName?: string,
        public nationality?: string,
        public gender?: Gender,
        public dateofBrith?: any,
        public educationLevel?: EducationLevel,
        public email?: string,
        public skype?: string,
        public phone?: string,
        public wechat?: string,
        public resumeId?: number,
    ) {
    }
}
