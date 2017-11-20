import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { MjobMojobs } from './mjob-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MjobMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/mjobs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/mjobs';

    constructor(private http: Http) { }

    create(mjob: MjobMojobs): Observable<MjobMojobs> {
        const copy = this.convert(mjob);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(mjob: MjobMojobs): Observable<MjobMojobs> {
        const copy = this.convert(mjob);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<MjobMojobs> {
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
     * Convert a returned JSON object to MjobMojobs.
     */
    private convertItemFromServer(json: any): MjobMojobs {
        const entity: MjobMojobs = Object.assign(new MjobMojobs(), json);
        return entity;
    }

    /**
     * Convert a MjobMojobs to a JSON which can be sent to the server.
     */
    private convert(mjob: MjobMojobs): MjobMojobs {
        const copy: MjobMojobs = Object.assign({}, mjob);
        return copy;
    }
}
