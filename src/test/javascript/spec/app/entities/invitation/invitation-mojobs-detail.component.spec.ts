/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InvitationMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/invitation/invitation-mojobs-detail.component';
import { InvitationMojobsService } from '../../../../../../main/webapp/app/entities/invitation/invitation-mojobs.service';
import { InvitationMojobs } from '../../../../../../main/webapp/app/entities/invitation/invitation-mojobs.model';

describe('Component Tests', () => {

    describe('InvitationMojobs Management Detail Component', () => {
        let comp: InvitationMojobsDetailComponent;
        let fixture: ComponentFixture<InvitationMojobsDetailComponent>;
        let service: InvitationMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [InvitationMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    InvitationMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(InvitationMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InvitationMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvitationMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new InvitationMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.invitation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
