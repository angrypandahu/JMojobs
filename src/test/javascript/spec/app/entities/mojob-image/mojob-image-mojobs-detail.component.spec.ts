/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MojobImageMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/mojob-image/mojob-image-mojobs-detail.component';
import { MojobImageMojobsService } from '../../../../../../main/webapp/app/entities/mojob-image/mojob-image-mojobs.service';
import { MojobImageMojobs } from '../../../../../../main/webapp/app/entities/mojob-image/mojob-image-mojobs.model';

describe('Component Tests', () => {

    describe('MojobImageMojobs Management Detail Component', () => {
        let comp: MojobImageMojobsDetailComponent;
        let fixture: ComponentFixture<MojobImageMojobsDetailComponent>;
        let service: MojobImageMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [MojobImageMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MojobImageMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(MojobImageMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MojobImageMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MojobImageMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MojobImageMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.mojobImage).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
