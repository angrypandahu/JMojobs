/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProfessionalDevelopmentMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/professional-development/professional-development-mojobs-detail.component';
import { ProfessionalDevelopmentMojobsService } from '../../../../../../main/webapp/app/entities/professional-development/professional-development-mojobs.service';
import { ProfessionalDevelopmentMojobs } from '../../../../../../main/webapp/app/entities/professional-development/professional-development-mojobs.model';

describe('Component Tests', () => {

    describe('ProfessionalDevelopmentMojobs Management Detail Component', () => {
        let comp: ProfessionalDevelopmentMojobsDetailComponent;
        let fixture: ComponentFixture<ProfessionalDevelopmentMojobsDetailComponent>;
        let service: ProfessionalDevelopmentMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ProfessionalDevelopmentMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProfessionalDevelopmentMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProfessionalDevelopmentMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfessionalDevelopmentMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfessionalDevelopmentMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProfessionalDevelopmentMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.professionalDevelopment).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
