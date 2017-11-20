/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProvinceMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/province/province-mojobs-detail.component';
import { ProvinceMojobsService } from '../../../../../../main/webapp/app/entities/province/province-mojobs.service';
import { ProvinceMojobs } from '../../../../../../main/webapp/app/entities/province/province-mojobs.model';

describe('Component Tests', () => {

    describe('ProvinceMojobs Management Detail Component', () => {
        let comp: ProvinceMojobsDetailComponent;
        let fixture: ComponentFixture<ProvinceMojobsDetailComponent>;
        let service: ProvinceMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ProvinceMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProvinceMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProvinceMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProvinceMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProvinceMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProvinceMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.province).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
