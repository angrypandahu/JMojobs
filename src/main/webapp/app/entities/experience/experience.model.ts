import { BaseEntity } from './../../shared';

export class Experience implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public school?: string,
        public grade?: string,
        public location?: string,
        public fromDate?: any,
        public toDate?: any,
        public currentlyWorkHere?: boolean,
        public responsibility?: string,
        public resumeId?: number,
    ) {
        this.currentlyWorkHere = false;
    }
}
