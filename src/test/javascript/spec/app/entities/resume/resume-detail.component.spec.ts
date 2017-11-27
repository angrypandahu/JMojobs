/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ResumeDetailComponent } from '../../../../../../main/webapp/app/entities/resume/resume-detail.component';
import { ResumeService } from '../../../../../../main/webapp/app/entities/resume/resume.service';
import { Resume } from '../../../../../../main/webapp/app/entities/resume/resume.model';

describe('Component Tests', () => {

    describe('Resume Management Detail Component', () => {
        let comp: ResumeDetailComponent;
        let fixture: ComponentFixture<ResumeDetailComponent>;
        let service: ResumeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ResumeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ResumeService,
                    JhiEventManager
                ]
            }).overrideTemplate(ResumeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ResumeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResumeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Resume(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.resume).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
