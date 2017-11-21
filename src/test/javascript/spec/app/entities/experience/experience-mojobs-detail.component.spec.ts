/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ExperienceMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/experience/experience-mojobs-detail.component';
import { ExperienceMojobsService } from '../../../../../../main/webapp/app/entities/experience/experience-mojobs.service';
import { ExperienceMojobs } from '../../../../../../main/webapp/app/entities/experience/experience-mojobs.model';

describe('Component Tests', () => {

    describe('ExperienceMojobs Management Detail Component', () => {
        let comp: ExperienceMojobsDetailComponent;
        let fixture: ComponentFixture<ExperienceMojobsDetailComponent>;
        let service: ExperienceMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ExperienceMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ExperienceMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ExperienceMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExperienceMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExperienceMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ExperienceMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.experience).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
