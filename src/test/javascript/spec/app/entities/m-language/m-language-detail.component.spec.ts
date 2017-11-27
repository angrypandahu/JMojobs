/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MLanguageDetailComponent } from '../../../../../../main/webapp/app/entities/m-language/m-language-detail.component';
import { MLanguageService } from '../../../../../../main/webapp/app/entities/m-language/m-language.service';
import { MLanguage } from '../../../../../../main/webapp/app/entities/m-language/m-language.model';

describe('Component Tests', () => {

    describe('MLanguage Management Detail Component', () => {
        let comp: MLanguageDetailComponent;
        let fixture: ComponentFixture<MLanguageDetailComponent>;
        let service: MLanguageService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [MLanguageDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MLanguageService,
                    JhiEventManager
                ]
            }).overrideTemplate(MLanguageDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MLanguageDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MLanguageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MLanguage(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.mLanguage).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
