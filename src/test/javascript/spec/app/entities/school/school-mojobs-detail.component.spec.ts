/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SchoolMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/school/school-mojobs-detail.component';
import { SchoolMojobsService } from '../../../../../../main/webapp/app/entities/school/school-mojobs.service';
import { SchoolMojobs } from '../../../../../../main/webapp/app/entities/school/school-mojobs.model';

describe('Component Tests', () => {

    describe('SchoolMojobs Management Detail Component', () => {
        let comp: SchoolMojobsDetailComponent;
        let fixture: ComponentFixture<SchoolMojobsDetailComponent>;
        let service: SchoolMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [SchoolMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SchoolMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(SchoolMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SchoolMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SchoolMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SchoolMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.school).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
