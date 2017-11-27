/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SchoolDetailComponent } from '../../../../../../main/webapp/app/entities/school/school-detail.component';
import { SchoolService } from '../../../../../../main/webapp/app/entities/school/school.service';
import { School } from '../../../../../../main/webapp/app/entities/school/school.model';

describe('Component Tests', () => {

    describe('School Management Detail Component', () => {
        let comp: SchoolDetailComponent;
        let fixture: ComponentFixture<SchoolDetailComponent>;
        let service: SchoolService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [SchoolDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SchoolService,
                    JhiEventManager
                ]
            }).overrideTemplate(SchoolDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SchoolDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SchoolService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new School(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.school).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
