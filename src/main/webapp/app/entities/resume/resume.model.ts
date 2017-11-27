import { BaseEntity } from './../../shared';

export const enum CanBeReadBy {
    'ALL_SCHOOLS',
    'UNBLOCKED_SCHOOLS',
    'ONLY_MYSELF'
}

export class Resume implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public canBeReadBy?: CanBeReadBy,
        public others?: string,
        public basicInformationId?: number,
        public experiencies?: BaseEntity[],
        public educationBackgrounds?: BaseEntity[],
        public professionalDevelopments?: BaseEntity[],
        public languages?: BaseEntity[],
        public userId?: number,
    ) {
    }
}
