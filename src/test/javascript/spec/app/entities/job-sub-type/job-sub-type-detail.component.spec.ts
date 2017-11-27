/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobSubTypeDetailComponent } from '../../../../../../main/webapp/app/entities/job-sub-type/job-sub-type-detail.component';
import { JobSubTypeService } from '../../../../../../main/webapp/app/entities/job-sub-type/job-sub-type.service';
import { JobSubType } from '../../../../../../main/webapp/app/entities/job-sub-type/job-sub-type.model';

describe('Component Tests', () => {

    describe('JobSubType Management Detail Component', () => {
        let comp: JobSubTypeDetailComponent;
        let fixture: ComponentFixture<JobSubTypeDetailComponent>;
        let service: JobSubTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [JobSubTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobSubTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobSubTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobSubTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobSubTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobSubType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobSubType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
