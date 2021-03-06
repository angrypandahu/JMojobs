import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProfessionalDevelopmentMojobs } from './professional-development-mojobs.model';
import { ProfessionalDevelopmentMojobsService } from './professional-development-mojobs.service';

@Injectable()
export class ProfessionalDevelopmentMojobsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private professionalDevelopmentService: ProfessionalDevelopmentMojobsService

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
                this.professionalDevelopmentService.find(id).subscribe((professionalDevelopment) => {
                    if (professionalDevelopment.fromDate) {
                        professionalDevelopment.fromDate = {
                            year: professionalDevelopment.fromDate.getFullYear(),
                            month: professionalDevelopment.fromDate.getMonth() + 1,
                            day: professionalDevelopment.fromDate.getDate()
                        };
                    }
                    if (professionalDevelopment.toDate) {
                        professionalDevelopment.toDate = {
                            year: professionalDevelopment.toDate.getFullYear(),
                            month: professionalDevelopment.toDate.getMonth() + 1,
                            day: professionalDevelopment.toDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.professionalDevelopmentModalRef(component, professionalDevelopment);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.professionalDevelopmentModalRef(component, new ProfessionalDevelopmentMojobs());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    professionalDevelopmentModalRef(component: Component, professionalDevelopment: ProfessionalDevelopmentMojobs): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.professionalDevelopment = professionalDevelopment;
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
