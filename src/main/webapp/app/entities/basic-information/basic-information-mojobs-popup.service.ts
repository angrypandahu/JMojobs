import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BasicInformationMojobs } from './basic-information-mojobs.model';
import { BasicInformationMojobsService } from './basic-information-mojobs.service';

@Injectable()
export class BasicInformationMojobsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private basicInformationService: BasicInformationMojobsService

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
                this.basicInformationService.find(id).subscribe((basicInformation) => {
                    if (basicInformation.dateofBrith) {
                        basicInformation.dateofBrith = {
                            year: basicInformation.dateofBrith.getFullYear(),
                            month: basicInformation.dateofBrith.getMonth() + 1,
                            day: basicInformation.dateofBrith.getDate()
                        };
                    }
                    this.ngbModalRef = this.basicInformationModalRef(component, basicInformation);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.basicInformationModalRef(component, new BasicInformationMojobs());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    basicInformationModalRef(component: Component, basicInformation: BasicInformationMojobs): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.basicInformation = basicInformation;
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
