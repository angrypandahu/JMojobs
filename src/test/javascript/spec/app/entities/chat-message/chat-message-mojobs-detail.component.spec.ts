/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmojobsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ChatMessageMojobsDetailComponent } from '../../../../../../main/webapp/app/entities/chat-message/chat-message-mojobs-detail.component';
import { ChatMessageMojobsService } from '../../../../../../main/webapp/app/entities/chat-message/chat-message-mojobs.service';
import { ChatMessageMojobs } from '../../../../../../main/webapp/app/entities/chat-message/chat-message-mojobs.model';

describe('Component Tests', () => {

    describe('ChatMessageMojobs Management Detail Component', () => {
        let comp: ChatMessageMojobsDetailComponent;
        let fixture: ComponentFixture<ChatMessageMojobsDetailComponent>;
        let service: ChatMessageMojobsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmojobsTestModule],
                declarations: [ChatMessageMojobsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ChatMessageMojobsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ChatMessageMojobsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChatMessageMojobsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChatMessageMojobsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ChatMessageMojobs(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.chatMessage).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
