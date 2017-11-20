/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MjobMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/mjob/mjob-mojobs-detail.component';
import { MjobMojobsService } from '../../../../../../main/webapp/app/entities/mjob/mjob-mojobs.service';
import { MjobMojobs } from '../../../../../../main/webapp/app/entities/mjob/mjob-mojobs.model';

describe('Component Tests', () => {

    describe('MjobMojobs Management Detail Component', () => {
        let comp: MjobMojobsDetailComponent;
        let fixture: ComponentFixture<MjobMojobsDetailComponent>;
        let service: MjobMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [MjobMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MjobMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(MjobMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MjobMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MjobMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MjobMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.mjob).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
