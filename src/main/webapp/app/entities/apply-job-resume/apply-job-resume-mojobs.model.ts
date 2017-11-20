import { BaseEntity } from './../../shared';

export class ApplyJobResumeMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public applyDate?: any,
        public content?: any,
        public resumeId?: number,
        public mjobId?: number,
    ) {
    }
}
