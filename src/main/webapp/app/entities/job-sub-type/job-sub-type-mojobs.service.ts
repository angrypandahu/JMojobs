import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobSubTypeMojobs } from './job-sub-type-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobSubTypeMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/job-sub-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-sub-types';

    constructor(private http: Http) { }

    create(jobSubType: JobSubTypeMojobs): Observable<JobSubTypeMojobs> {
        const copy = this.convert(jobSubType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobSubType: JobSubTypeMojobs): Observable<JobSubTypeMojobs> {
        const copy = this.convert(jobSubType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobSubTypeMojobs> {
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
     * Convert a returned JSON object to JobSubTypeMojobs.
     */
    private convertItemFromServer(json: any): JobSubTypeMojobs {
        const entity: JobSubTypeMojobs = Object.assign(new JobSubTypeMojobs(), json);
        return entity;
    }

    /**
     * Convert a JobSubTypeMojobs to a JSON which can be sent to the server.
     */
    private convert(jobSubType: JobSubTypeMojobs): JobSubTypeMojobs {
        const copy: JobSubTypeMojobs = Object.assign({}, jobSubType);
        return copy;
    }
}
