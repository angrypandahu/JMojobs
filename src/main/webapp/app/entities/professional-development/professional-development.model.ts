import { BaseEntity } from './../../shared';

export class ProfessionalDevelopment implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public fromDate?: any,
        public toDate?: any,
        public resumeId?: number,
    ) {
    }
}
