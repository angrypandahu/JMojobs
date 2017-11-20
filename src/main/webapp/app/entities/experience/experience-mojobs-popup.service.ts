import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ExperienceMojobs } from './experience-mojobs.model';
import { ExperienceMojobsService } from './experience-mojobs.service';

@Injectable()
export class ExperienceMojobsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private experienceService: ExperienceMojobsService

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
                this.experienceService.find(id).subscribe((experience) => {
                    if (experience.fromDate) {
                        experience.fromDate = {
                            year: experience.fromDate.getFullYear(),
                            month: experience.fromDate.getMonth() + 1,
                            day: experience.fromDate.getDate()
                        };
                    }
                    if (experience.toDate) {
                        experience.toDate = {
                            year: experience.toDate.getFullYear(),
                            month: experience.toDate.getMonth() + 1,
                            day: experience.toDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.experienceModalRef(component, experience);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.experienceModalRef(component, new ExperienceMojobs());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    experienceModalRef(component: Component, experience: ExperienceMojobs): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.experience = experience;
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
