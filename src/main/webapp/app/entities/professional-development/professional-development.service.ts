import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ProfessionalDevelopment } from './professional-development.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProfessionalDevelopmentService {

    private resourceUrl = SERVER_API_URL + 'api/professional-developments';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/professional-developments';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(professionalDevelopment: ProfessionalDevelopment): Observable<ProfessionalDevelopment> {
        const copy = this.convert(professionalDevelopment);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(professionalDevelopment: ProfessionalDevelopment): Observable<ProfessionalDevelopment> {
        const copy = this.convert(professionalDevelopment);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ProfessionalDevelopment> {
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
     * Convert a returned JSON object to ProfessionalDevelopment.
     */
    private convertItemFromServer(json: any): ProfessionalDevelopment {
        const entity: ProfessionalDevelopment = Object.assign(new ProfessionalDevelopment(), json);
        entity.fromDate = this.dateUtils
            .convertLocalDateFromServer(json.fromDate);
        entity.toDate = this.dateUtils
            .convertLocalDateFromServer(json.toDate);
        return entity;
    }

    /**
     * Convert a ProfessionalDevelopment to a JSON which can be sent to the server.
     */
    private convert(professionalDevelopment: ProfessionalDevelopment): ProfessionalDevelopment {
        const copy: ProfessionalDevelopment = Object.assign({}, professionalDevelopment);
        copy.fromDate = this.dateUtils
            .convertLocalDateToServer(professionalDevelopment.fromDate);
        copy.toDate = this.dateUtils
            .convertLocalDateToServer(professionalDevelopment.toDate);
        return copy;
    }
}
