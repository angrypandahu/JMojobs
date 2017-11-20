/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AddressMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/address/address-mojobs-detail.component';
import { AddressMojobsService } from '../../../../../../main/webapp/app/entities/address/address-mojobs.service';
import { AddressMojobs } from '../../../../../../main/webapp/app/entities/address/address-mojobs.model';

describe('Component Tests', () => {

    describe('AddressMojobs Management Detail Component', () => {
        let comp: AddressMojobsDetailComponent;
        let fixture: ComponentFixture<AddressMojobsDetailComponent>;
        let service: AddressMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [AddressMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AddressMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(AddressMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AddressMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AddressMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AddressMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.address).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
