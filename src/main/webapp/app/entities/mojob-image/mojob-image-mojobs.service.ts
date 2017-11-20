import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { MojobImageMojobs } from './mojob-image-mojobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MojobImageMojobsService {

    private resourceUrl = SERVER_API_URL + 'api/mojob-images';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/mojob-images';

    constructor(private http: Http) { }

    create(mojobImage: MojobImageMojobs): Observable<MojobImageMojobs> {
        const copy = this.convert(mojobImage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(mojobImage: MojobImageMojobs): Observable<MojobImageMojobs> {
        const copy = this.convert(mojobImage);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<MojobImageMojobs> {
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
     * Convert a returned JSON object to MojobImageMojobs.
     */
    private convertItemFromServer(json: any): MojobImageMojobs {
        const entity: MojobImageMojobs = Object.assign(new MojobImageMojobs(), json);
        return entity;
    }

    /**
     * Convert a MojobImageMojobs to a JSON which can be sent to the server.
     */
    private convert(mojobImage: MojobImageMojobs): MojobImageMojobs {
        const copy: MojobImageMojobs = Object.assign({}, mojobImage);
        return copy;
    }
}
