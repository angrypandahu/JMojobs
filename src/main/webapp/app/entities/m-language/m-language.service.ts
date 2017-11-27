import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { MLanguage } from './m-language.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MLanguageService {

    private resourceUrl = SERVER_API_URL + 'api/m-languages';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/m-languages';

    constructor(private http: Http) { }

    create(mLanguage: MLanguage): Observable<MLanguage> {
        const copy = this.convert(mLanguage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(mLanguage: MLanguage): Observable<MLanguage> {
        const copy = this.convert(mLanguage);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<MLanguage> {
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
     * Convert a returned JSON object to MLanguage.
     */
    private convertItemFromServer(json: any): MLanguage {
        const entity: MLanguage = Object.assign(new MLanguage(), json);
        return entity;
    }

    /**
     * Convert a MLanguage to a JSON which can be sent to the server.
     */
    private convert(mLanguage: MLanguage): MLanguage {
        const copy: MLanguage = Object.assign({}, mLanguage);
        return copy;
    }
}
