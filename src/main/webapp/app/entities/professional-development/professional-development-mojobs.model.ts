import { BaseEntity } from './../../shared';

export class ProfessionalDevelopmentMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public fromDate?: any,
        public toDate?: any,
        public resumeId?: number,
    ) {
    }
}
