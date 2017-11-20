import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { SchoolMojobs } from './school-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SchoolMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/schools';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/schools';

    constructor(private http: Http) { }

    create(school: SchoolMojobs): Observable<SchoolMojobs> {
        const copy = this.convert(school);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(school: SchoolMojobs): Observable<SchoolMojobs> {
        const copy = this.convert(school);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SchoolMojobs> {
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
     * Convert a returned JSON object to SchoolMojobs.
     */
    private convertItemFromServer(json: any): SchoolMojobs {
        const entity: SchoolMojobs = Object.assign(new SchoolMojobs(), json);
        return entity;
    }

    /**
     * Convert a SchoolMojobs to a JSON which can be sent to the server.
     */
    private convert(school: SchoolMojobs): SchoolMojobs {
        const copy: SchoolMojobs = Object.assign({}, school);
        return copy;
    }
}
