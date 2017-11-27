/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MjobDetailComponent } from '../../../../../../main/webapp/app/entities/mjob/mjob-detail.component';
import { MjobService } from '../../../../../../main/webapp/app/entities/mjob/mjob.service';
import { Mjob } from '../../../../../../main/webapp/app/entities/mjob/mjob.model';

describe('Component Tests', () => {

    describe('Mjob Management Detail Component', () => {
        let comp: MjobDetailComponent;
        let fixture: ComponentFixture<MjobDetailComponent>;
        let service: MjobService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [MjobDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MjobService,
                    JhiEventManager
                ]
            }).overrideTemplate(MjobDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MjobDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MjobService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Mjob(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.mjob).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
