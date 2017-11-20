import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JmojobsJobSubTypeMojobsModule } from './job-sub-type/job-sub-type-mojobs.module';
import { JmojobsProvinceMojobsModule } from './province/province-mojobs.module';
import { JmojobsCityMojobsModule } from './city/city-mojobs.module';
import { JmojobsTownMojobsModule } from './town/town-mojobs.module';
import { JmojobsAddressMojobsModule } from './address/address-mojobs.module';
import { JmojobsMjobMojobsModule } from './mjob/mjob-mojobs.module';
import { JmojobsSchoolMojobsModule } from './school/school-mojobs.module';
import { JmojobsMojobImageMojobsModule } from './mojob-image/mojob-image-mojobs.module';
import { JmojobsResumeMojobsModule } from './resume/resume-mojobs.module';
import { JmojobsBasicInformationMojobsModule } from './basic-information/basic-information-mojobs.module';
import { JmojobsExperienceMojobsModule } from './experience/experience-mojobs.module';
import { JmojobsEducationBackgroundMojobsModule } from './education-background/education-background-mojobs.module';
import { JmojobsProfessionalDevelopmentMojobsModule } from './professional-development/professional-development-mojobs.module';
import { JmojobsMLanguageMojobsModule } from './m-language/m-language-mojobs.module';
import { JmojobsInvitationMojobsModule } from './invitation/invitation-mojobs.module';
import { JmojobsApplyJobResumeMojobsModule } from './apply-job-resume/apply-job-resume-mojobs.module';
import { JmojobsChatMessageMojobsModule } from './chat-message/chat-message-mojobs.module';
import { JmojobsSchoolAddressMojobsModule } from './school-address/school-address-mojobs.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JmojobsJobSubTypeMojobsModule,
        JmojobsProvinceMojobsModule,
        JmojobsCityMojobsModule,
        JmojobsTownMojobsModule,
        JmojobsAddressMojobsModule,
        JmojobsMjobMojobsModule,
        JmojobsSchoolMojobsModule,
        JmojobsMojobImageMojobsModule,
        JmojobsResumeMojobsModule,
        JmojobsBasicInformationMojobsModule,
        JmojobsExperienceMojobsModule,
        JmojobsEducationBackgroundMojobsModule,
        JmojobsProfessionalDevelopmentMojobsModule,
        JmojobsMLanguageMojobsModule,
        JmojobsInvitationMojobsModule,
        JmojobsApplyJobResumeMojobsModule,
        JmojobsChatMessageMojobsModule,
        JmojobsSchoolAddressMojobsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmojobsEntityModule {}
