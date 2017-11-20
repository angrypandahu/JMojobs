import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EducationBackgroundMojobs } from './education-background-mojobs.model';
import { EducationBackgroundMojobsService } from './education-background-mojobs.service';

@Injectable()
export class EducationBackgroundMojobsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private educationBackgroundService: EducationBackgroundMojobsService

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
                this.educationBackgroundService.find(id).subscribe((educationBackground) => {
                    if (educationBackground.fromDate) {
                        educationBackground.fromDate = {
                            year: educationBackground.fromDate.getFullYear(),
                            month: educationBackground.fromDate.getMonth() + 1,
                            day: educationBackground.fromDate.getDate()
                        };
                    }
                    if (educationBackground.toDate) {
                        educationBackground.toDate = {
                            year: educationBackground.toDate.getFullYear(),
                            month: educationBackground.toDate.getMonth() + 1,
                            day: educationBackground.toDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.educationBackgroundModalRef(component, educationBackground);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.educationBackgroundModalRef(component, new EducationBackgroundMojobs());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    educationBackgroundModalRef(component: Component, educationBackground: EducationBackgroundMojobs): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.educationBackground = educationBackground;
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
