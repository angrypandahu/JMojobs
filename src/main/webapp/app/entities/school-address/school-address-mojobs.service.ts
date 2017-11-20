import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { SchoolAddressMojobs } from './school-address-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SchoolAddressMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/school-addresses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/school-addresses';

    constructor(private http: Http) { }

    create(schoolAddress: SchoolAddressMojobs): Observable<SchoolAddressMojobs> {
        const copy = this.convert(schoolAddress);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(schoolAddress: SchoolAddressMojobs): Observable<SchoolAddressMojobs> {
        const copy = this.convert(schoolAddress);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SchoolAddressMojobs> {
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
     * Convert a returned JSON object to SchoolAddressMojobs.
     */
    private convertItemFromServer(json: any): SchoolAddressMojobs {
        const entity: SchoolAddressMojobs = Object.assign(new SchoolAddressMojobs(), json);
        return entity;
    }

    /**
     * Convert a SchoolAddressMojobs to a JSON which can be sent to the server.
     */
    private convert(schoolAddress: SchoolAddressMojobs): SchoolAddressMojobs {
        const copy: SchoolAddressMojobs = Object.assign({}, schoolAddress);
        return copy;
    }
}
