/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TownMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/town/town-mojobs-detail.component';
import { TownMojobsService } from '../../../../../../main/webapp/app/entities/town/town-mojobs.service';
import { TownMojobs } from '../../../../../../main/webapp/app/entities/town/town-mojobs.model';

describe('Component Tests', () => {

    describe('TownMojobs Management Detail Component', () => {
        let comp: TownMojobsDetailComponent;
        let fixture: ComponentFixture<TownMojobsDetailComponent>;
        let service: TownMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [TownMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TownMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(TownMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TownMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TownMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TownMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.town).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
