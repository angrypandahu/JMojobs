import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { EducationBackground } from './education-background.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EducationBackgroundService {

    private resourceUrl = SERVER_API_URL + 'api/education-backgrounds';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/education-backgrounds';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(educationBackground: EducationBackground): Observable<EducationBackground> {
        const copy = this.convert(educationBackground);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(educationBackground: EducationBackground): Observable<EducationBackground> {
        const copy = this.convert(educationBackground);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<EducationBackground> {
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
     * Convert a returned JSON object to EducationBackground.
     */
    private convertItemFromServer(json: any): EducationBackground {
        const entity: EducationBackground = Object.assign(new EducationBackground(), json);
        entity.fromDate = this.dateUtils
            .convertLocalDateFromServer(json.fromDate);
        entity.toDate = this.dateUtils
            .convertLocalDateFromServer(json.toDate);
        return entity;
    }

    /**
     * Convert a EducationBackground to a JSON which can be sent to the server.
     */
    private convert(educationBackground: EducationBackground): EducationBackground {
        const copy: EducationBackground = Object.assign({}, educationBackground);
        copy.fromDate = this.dateUtils
            .convertLocalDateToServer(educationBackground.fromDate);
        copy.toDate = this.dateUtils
            .convertLocalDateToServer(educationBackground.toDate);
        return copy;
    }
}
