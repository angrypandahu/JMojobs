/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EducationBackgroundMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/education-background/education-background-mojobs-detail.component';
import { EducationBackgroundMojobsService } from '../../../../../../main/webapp/app/entities/education-background/education-background-mojobs.service';
import { EducationBackgroundMojobs } from '../../../../../../main/webapp/app/entities/education-background/education-background-mojobs.model';

describe('Component Tests', () => {

    describe('EducationBackgroundMojobs Management Detail Component', () => {
        let comp: EducationBackgroundMojobsDetailComponent;
        let fixture: ComponentFixture<EducationBackgroundMojobsDetailComponent>;
        let service: EducationBackgroundMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [EducationBackgroundMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EducationBackgroundMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(EducationBackgroundMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EducationBackgroundMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EducationBackgroundMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EducationBackgroundMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.educationBackground).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
