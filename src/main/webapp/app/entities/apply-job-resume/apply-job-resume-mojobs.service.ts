import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ApplyJobResumeMojobs } from './apply-job-resume-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ApplyJobResumeMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/apply-job-resumes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/apply-job-resumes';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(applyJobResume: ApplyJobResumeMojobs): Observable<ApplyJobResumeMojobs> {
        const copy = this.convert(applyJobResume);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(applyJobResume: ApplyJobResumeMojobs): Observable<ApplyJobResumeMojobs> {
        const copy = this.convert(applyJobResume);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ApplyJobResumeMojobs> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to ApplyJobResumeMojobs.
     */
    private convertItemFromServer(json: any): ApplyJobResumeMojobs {
        const entity: ApplyJobResumeMojobs = Object.assign(new ApplyJobResumeMojobs(), json);
        entity.applyDate = this.dateUtils
            .convertLocalDateFromServer(json.applyDate);
        return entity;
    }

    /**
     * Convert a ApplyJobResumeMojobs to a JSON which can be sent to the server.
     */
    private convert(applyJobResume: ApplyJobResumeMojobs): ApplyJobResumeMojobs {
        const copy: ApplyJobResumeMojobs = Object.assign({}, applyJobResume);
        copy.applyDate = this.dateUtils
            .convertLocalDateToServer(applyJobResume.applyDate);
        return copy;
    }
}
