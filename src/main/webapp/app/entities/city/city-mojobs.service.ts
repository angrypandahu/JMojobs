import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { CityMojobs } from './city-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CityMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/cities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cities';

    constructor(private http: Http) { }

    create(city: CityMojobs): Observable<CityMojobs> {
        const copy = this.convert(city);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(city: CityMojobs): Observable<CityMojobs> {
        const copy = this.convert(city);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<CityMojobs> {
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
     * Convert a returned JSON object to CityMojobs.
     */
    private convertItemFromServer(json: any): CityMojobs {
        const entity: CityMojobs = Object.assign(new CityMojobs(), json);
        return entity;
    }

    /**
     * Convert a CityMojobs to a JSON which can be sent to the server.
     */
    private convert(city: CityMojobs): CityMojobs {
        const copy: CityMojobs = Object.assign({}, city);
        return copy;
    }
}
