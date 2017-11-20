/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BasicInformationMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/basic-information/basic-information-mojobs-detail.component';
import { BasicInformationMojobsService } from '../../../../../../main/webapp/app/entities/basic-information/basic-information-mojobs.service';
import { BasicInformationMojobs } from '../../../../../../main/webapp/app/entities/basic-information/basic-information-mojobs.model';

describe('Component Tests', () => {

    describe('BasicInformationMojobs Management Detail Component', () => {
        let comp: BasicInformationMojobsDetailComponent;
        let fixture: ComponentFixture<BasicInformationMojobsDetailComponent>;
        let service: BasicInformationMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [BasicInformationMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BasicInformationMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(BasicInformationMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BasicInformationMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BasicInformationMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BasicInformationMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.basicInformation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
