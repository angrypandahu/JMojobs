import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobAddressMojobs } from './job-address-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobAddressMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/job-addresses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-addresses';

    constructor(private http: Http) { }

    create(jobAddress: JobAddressMojobs): Observable<JobAddressMojobs> {
        const copy = this.convert(jobAddress);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobAddress: JobAddressMojobs): Observable<JobAddressMojobs> {
        const copy = this.convert(jobAddress);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobAddressMojobs> {
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
     * Convert a returned JSON object to JobAddressMojobs.
     */
    private convertItemFromServer(json: any): JobAddressMojobs {
        const entity: JobAddressMojobs = Object.assign(new JobAddressMojobs(), json);
        return entity;
    }

    /**
     * Convert a JobAddressMojobs to a JSON which can be sent to the server.
     */
    private convert(jobAddress: JobAddressMojobs): JobAddressMojobs {
        const copy: JobAddressMojobs = Object.assign({}, jobAddress);
        return copy;
    }
}
