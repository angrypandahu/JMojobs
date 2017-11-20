/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ResumeMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/resume/resume-mojobs-detail.component';
import { ResumeMojobsService } from '../../../../../../main/webapp/app/entities/resume/resume-mojobs.service';
import { ResumeMojobs } from '../../../../../../main/webapp/app/entities/resume/resume-mojobs.model';

describe('Component Tests', () => {

    describe('ResumeMojobs Management Detail Component', () => {
        let comp: ResumeMojobsDetailComponent;
        let fixture: ComponentFixture<ResumeMojobsDetailComponent>;
        let service: ResumeMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ResumeMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ResumeMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ResumeMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ResumeMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResumeMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ResumeMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.resume).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
