/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EducationBackgroundDetailComponent } from '../../../../../../main/webapp/app/entities/education-background/education-background-detail.component';
import { EducationBackgroundService } from '../../../../../../main/webapp/app/entities/education-background/education-background.service';
import { EducationBackground } from '../../../../../../main/webapp/app/entities/education-background/education-background.model';

describe('Component Tests', () => {

    describe('EducationBackground Management Detail Component', () => {
        let comp: EducationBackgroundDetailComponent;
        let fixture: ComponentFixture<EducationBackgroundDetailComponent>;
        let service: EducationBackgroundService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [EducationBackgroundDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EducationBackgroundService,
                    JhiEventManager
                ]
            }).overrideTemplate(EducationBackgroundDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EducationBackgroundDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EducationBackgroundService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EducationBackground(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.educationBackground).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
