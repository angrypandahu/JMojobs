import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Invitation } from './invitation.model';
import { InvitationService } from './invitation.service';

@Injectable()
export class InvitationPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private invitationService: InvitationService

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
                this.invitationService.find(id).subscribe((invitation) => {
                    if (invitation.fromDate) {
                        invitation.fromDate = {
                            year: invitation.fromDate.getFullYear(),
                            month: invitation.fromDate.getMonth() + 1,
                            day: invitation.fromDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.invitationModalRef(component, invitation);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.invitationModalRef(component, new Invitation());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    invitationModalRef(component: Component, invitation: Invitation): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.invitation = invitation;
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
