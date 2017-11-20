/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SchoolAddressMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/school-address/school-address-mojobs-detail.component';
import { SchoolAddressMojobsService } from '../../../../../../main/webapp/app/entities/school-address/school-address-mojobs.service';
import { SchoolAddressMojobs } from '../../../../../../main/webapp/app/entities/school-address/school-address-mojobs.model';

describe('Component Tests', () => {

    describe('SchoolAddressMojobs Management Detail Component', () => {
        let comp: SchoolAddressMojobsDetailComponent;
        let fixture: ComponentFixture<SchoolAddressMojobsDetailComponent>;
        let service: SchoolAddressMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [SchoolAddressMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SchoolAddressMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(SchoolAddressMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SchoolAddressMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SchoolAddressMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SchoolAddressMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.schoolAddress).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
