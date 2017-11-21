/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MLanguageMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/m-language/m-language-mojobs-detail.component';
import { MLanguageMojobsService } from '../../../../../../main/webapp/app/entities/m-language/m-language-mojobs.service';
import { MLanguageMojobs } from '../../../../../../main/webapp/app/entities/m-language/m-language-mojobs.model';

describe('Component Tests', () => {

    describe('MLanguageMojobs Management Detail Component', () => {
        let comp: MLanguageMojobsDetailComponent;
        let fixture: ComponentFixture<MLanguageMojobsDetailComponent>;
        let service: MLanguageMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [MLanguageMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MLanguageMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(MLanguageMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MLanguageMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MLanguageMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MLanguageMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.mLanguage).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
