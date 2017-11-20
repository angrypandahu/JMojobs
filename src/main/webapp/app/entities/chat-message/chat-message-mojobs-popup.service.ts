import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ChatMessageMojobs } from './chat-message-mojobs.model';
import { ChatMessageMojobsService } from './chat-message-mojobs.service';

@Injectable()
export class ChatMessageMojobsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private chatMessageService: ChatMessageMojobsService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.chatMessageService.find(id).subscribe((chatMessage) => {
                    if (chatMessage.sendTime) {
                        chatMessage.sendTime = {
                            year: chatMessage.sendTime.getFullYear(),
                            month: chatMessage.sendTime.getMonth() + 1,
                            day: chatMessage.sendTime.getDate()
                        };
                    }
                    this.ngbModalRef = this.chatMessageModalRef(component, chatMessage);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.chatMessageModalRef(component, new ChatMessageMojobs());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    chatMessageModalRef(component: Component, chatMessage: ChatMessageMojobs): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.chatMessage = chatMessage;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
