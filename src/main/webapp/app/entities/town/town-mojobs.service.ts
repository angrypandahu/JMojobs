import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { TownMojobs } from './town-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TownMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/towns';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/towns';

    constructor(private http: Http) { }

    create(town: TownMojobs): Observable<TownMojobs> {
        const copy = this.convert(town);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(town: TownMojobs): Observable<TownMojobs> {
        const copy = this.convert(town);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<TownMojobs> {
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
     * Convert a returned JSON object to TownMojobs.
     */
    private convertItemFromServer(json: any): TownMojobs {
        const entity: TownMojobs = Object.assign(new TownMojobs(), json);
        return entity;
    }

    /**
     * Convert a TownMojobs to a JSON which can be sent to the server.
     */
    private convert(town: TownMojobs): TownMojobs {
        const copy: TownMojobs = Object.assign({}, town);
        return copy;
    }
}
