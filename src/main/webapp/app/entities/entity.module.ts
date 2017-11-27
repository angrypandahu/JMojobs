import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JmojobsJobSubTypeModule } from './job-sub-type/job-sub-type.module';
import { JmojobsAddressModule } from './address/address.module';
import { JmojobsMjobModule } from './mjob/mjob.module';
import { JmojobsSchoolModule } from './school/school.module';
import { JmojobsResumeModule } from './resume/resume.module';
import { JmojobsBasicInformationModule } from './basic-information/basic-information.module';
import { JmojobsExperienceModule } from './experience/experience.module';
import { JmojobsEducationBackgroundModule } from './education-background/education-background.module';
import { JmojobsProfessionalDevelopmentModule } from './professional-development/professional-development.module';
import { JmojobsMLanguageModule } from './m-language/m-language.module';
import { JmojobsInvitationModule } from './invitation/invitation.module';
import { JmojobsApplyJobResumeModule } from './apply-job-resume/apply-job-resume.module';
import { JmojobsChatMessageModule } from './chat-message/chat-message.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JmojobsJobSubTypeModule,
        JmojobsAddressModule,
        JmojobsMjobModule,
        JmojobsSchoolModule,
        JmojobsResumeModule,
        JmojobsBasicInformationModule,
        JmojobsExperienceModule,
        JmojobsEducationBackgroundModule,
        JmojobsProfessionalDevelopmentModule,
        JmojobsMLanguageModule,
        JmojobsInvitationModule,
        JmojobsApplyJobResumeModule,
        JmojobsChatMessageModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsEntityModule {}
