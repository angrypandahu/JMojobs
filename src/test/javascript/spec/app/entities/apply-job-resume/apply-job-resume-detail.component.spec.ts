/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ApplyJobResumeDetailComponent } from '../../../../../../main/webapp/app/entities/apply-job-resume/apply-job-resume-detail.component';
import { ApplyJobResumeService } from '../../../../../../main/webapp/app/entities/apply-job-resume/apply-job-resume.service';
import { ApplyJobResume } from '../../../../../../main/webapp/app/entities/apply-job-resume/apply-job-resume.model';

describe('Component Tests', () => {

    describe('ApplyJobResume Management Detail Component', () => {
        let comp: ApplyJobResumeDetailComponent;
        let fixture: ComponentFixture<ApplyJobResumeDetailComponent>;
        let service: ApplyJobResumeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ApplyJobResumeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ApplyJobResumeService,
                    JhiEventManager
                ]
            }).overrideTemplate(ApplyJobResumeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ApplyJobResumeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApplyJobResumeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ApplyJobResume(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.applyJobResume).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
