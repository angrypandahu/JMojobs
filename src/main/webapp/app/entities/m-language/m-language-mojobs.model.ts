import { BaseEntity } from './../../shared';

export const enum Language {
    'ENGLISH',
    'CHINESE',
    'JAPANESE'
}

export const enum LanguageLevel {
    'NATIVE_SPEAKER',
    'ADVANCED',
    'AVERAGE',
    'BEGINNER'
}

export class MLanguageMojobs implements BaseEntity {
    constructor(
        public id?: number,
        public name?: Language,
        public level?: LanguageLevel,
        public resumeId?: number,
    ) {
    }
}
