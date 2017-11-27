import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ApplyJobResume } from './apply-job-resume.model';
import { ApplyJobResumeService } from './apply-job-resume.service';

@Injectable()
export class ApplyJobResumePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private applyJobResumeService: ApplyJobResumeService

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
                this.applyJobResumeService.find(id).subscribe((applyJobResume) => {
                    if (applyJobResume.applyDate) {
                        applyJobResume.applyDate = {
                            year: applyJobResume.applyDate.getFullYear(),
                            month: applyJobResume.applyDate.getMonth() + 1,
                            day: applyJobResume.applyDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.applyJobResumeModalRef(component, applyJobResume);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.applyJobResumeModalRef(component, new ApplyJobResume());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    applyJobResumeModalRef(component: Component, applyJobResume: ApplyJobResume): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.applyJobResume = applyJobResume;
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
