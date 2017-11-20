/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobAddressMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/job-address/job-address-mojobs-detail.component';
import { JobAddressMojobsService } from '../../../../../../main/webapp/app/entities/job-address/job-address-mojobs.service';
import { JobAddressMojobs } from '../../../../../../main/webapp/app/entities/job-address/job-address-mojobs.model';

describe('Component Tests', () => {

    describe('JobAddressMojobs Management Detail Component', () => {
        let comp: JobAddressMojobsDetailComponent;
        let fixture: ComponentFixture<JobAddressMojobsDetailComponent>;
        let service: JobAddressMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [JobAddressMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobAddressMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobAddressMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobAddressMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobAddressMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobAddressMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobAddress).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
