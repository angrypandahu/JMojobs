import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { BasicInformation } from './basic-information.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BasicInformationService {

    private resourceUrl = SERVER_API_URL + 'api/basic-informations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/basic-informations';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(basicInformation: BasicInformation): Observable<BasicInformation> {
        const copy = this.convert(basicInformation);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(basicInformation: BasicInformation): Observable<BasicInformation> {
        const copy = this.convert(basicInformation);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<BasicInformation> {
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
     * Convert a returned JSON object to BasicInformation.
     */
    private convertItemFromServer(json: any): BasicInformation {
        const entity: BasicInformation = Object.assign(new BasicInformation(), json);
        entity.dateofBrith = this.dateUtils
            .convertLocalDateFromServer(json.dateofBrith);
        return entity;
    }

    /**
     * Convert a BasicInformation to a JSON which can be sent to the server.
     */
    private convert(basicInformation: BasicInformation): BasicInformation {
        const copy: BasicInformation = Object.assign({}, basicInformation);
        copy.dateofBrith = this.dateUtils
            .convertLocalDateToServer(basicInformation.dateofBrith);
        return copy;
    }
}
