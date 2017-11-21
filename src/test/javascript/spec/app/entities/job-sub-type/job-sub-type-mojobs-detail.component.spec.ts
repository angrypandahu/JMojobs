/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobSubTypeMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/job-sub-type/job-sub-type-mojobs-detail.component';
import { JobSubTypeMojobsService } from '../../../../../../main/webapp/app/entities/job-sub-type/job-sub-type-mojobs.service';
import { JobSubTypeMojobs } from '../../../../../../main/webapp/app/entities/job-sub-type/job-sub-type-mojobs.model';

describe('Component Tests', () => {

    describe('JobSubTypeMojobs Management Detail Component', () => {
        let comp: JobSubTypeMojobsDetailComponent;
        let fixture: ComponentFixture<JobSubTypeMojobsDetailComponent>;
        let service: JobSubTypeMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [JobSubTypeMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobSubTypeMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobSubTypeMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobSubTypeMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobSubTypeMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobSubTypeMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobSubType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
