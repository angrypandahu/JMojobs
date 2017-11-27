/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BasicInformationDetailComponent } from '../../../../../../main/webapp/app/entities/basic-information/basic-information-detail.component';
import { BasicInformationService } from '../../../../../../main/webapp/app/entities/basic-information/basic-information.service';
import { BasicInformation } from '../../../../../../main/webapp/app/entities/basic-information/basic-information.model';

describe('Component Tests', () => {

    describe('BasicInformation Management Detail Component', () => {
        let comp: BasicInformationDetailComponent;
        let fixture: ComponentFixture<BasicInformationDetailComponent>;
        let service: BasicInformationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [BasicInformationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BasicInformationService,
                    JhiEventManager
                ]
            }).overrideTemplate(BasicInformationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BasicInformationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BasicInformationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BasicInformation(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.basicInformation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
