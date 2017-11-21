/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CityMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/city/city-mojobs-detail.component';
import { CityMojobsService } from '../../../../../../main/webapp/app/entities/city/city-mojobs.service';
import { CityMojobs } from '../../../../../../main/webapp/app/entities/city/city-mojobs.model';

describe('Component Tests', () => {

    describe('CityMojobs Management Detail Component', () => {
        let comp: CityMojobsDetailComponent;
        let fixture: ComponentFixture<CityMojobsDetailComponent>;
        let service: CityMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [CityMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CityMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(CityMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CityMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CityMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CityMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.city).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
