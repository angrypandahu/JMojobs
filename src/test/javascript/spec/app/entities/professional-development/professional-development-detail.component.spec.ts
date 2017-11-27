/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProfessionalDevelopmentDetailComponent } from '../../../../../../main/webapp/app/entities/professional-development/professional-development-detail.component';
import { ProfessionalDevelopmentService } from '../../../../../../main/webapp/app/entities/professional-development/professional-development.service';
import { ProfessionalDevelopment } from '../../../../../../main/webapp/app/entities/professional-development/professional-development.model';

describe('Component Tests', () => {

    describe('ProfessionalDevelopment Management Detail Component', () => {
        let comp: ProfessionalDevelopmentDetailComponent;
        let fixture: ComponentFixture<ProfessionalDevelopmentDetailComponent>;
        let service: ProfessionalDevelopmentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ProfessionalDevelopmentDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProfessionalDevelopmentService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProfessionalDevelopmentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfessionalDevelopmentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfessionalDevelopmentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProfessionalDevelopment(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.professionalDevelopment).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
