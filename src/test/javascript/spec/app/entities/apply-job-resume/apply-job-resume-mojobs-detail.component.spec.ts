/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ApplyJobResumeMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/apply-job-resume/apply-job-resume-mojobs-detail.component';
import { ApplyJobResumeMojobsService } from '../../../../../../main/webapp/app/entities/apply-job-resume/apply-job-resume-mojobs.service';
import { ApplyJobResumeMojobs } from '../../../../../../main/webapp/app/entities/apply-job-resume/apply-job-resume-mojobs.model';

describe('Component Tests', () => {

    describe('ApplyJobResumeMojobs Management Detail Component', () => {
        let comp: ApplyJobResumeMojobsDetailComponent;
        let fixture: ComponentFixture<ApplyJobResumeMojobsDetailComponent>;
        let service: ApplyJobResumeMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ApplyJobResumeMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ApplyJobResumeMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ApplyJobResumeMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ApplyJobResumeMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApplyJobResumeMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ApplyJobResumeMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.applyJobResume).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
