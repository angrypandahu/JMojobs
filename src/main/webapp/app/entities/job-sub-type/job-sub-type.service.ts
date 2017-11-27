import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobSubType } from './job-sub-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobSubTypeService {

    private resourceUrl = SERVER_API_URL + 'api/job-sub-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-sub-types';

    constructor(private http: Http) { }

    create(jobSubType: JobSubType): Observable<JobSubType> {
        const copy = this.convert(jobSubType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobSubType: JobSubType): Observable<JobSubType> {
        const copy = this.convert(jobSubType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobSubType> {
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
     * Convert a returned JSON object to JobSubType.
     */
    private convertItemFromServer(json: any): JobSubType {
        const entity: JobSubType = Object.assign(new JobSubType(), json);
        return entity;
    }

    /**
     * Convert a JobSubType to a JSON which can be sent to the server.
     */
    private convert(jobSubType: JobSubType): JobSubType {
        const copy: JobSubType = Object.assign({}, jobSubType);
        return copy;
    }
}
