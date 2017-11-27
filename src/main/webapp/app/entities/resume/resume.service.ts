import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Resume } from './resume.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ResumeService {

    private resourceUrl = SERVER_API_URL + 'api/resumes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/resumes';

    constructor(private http: Http) { }

    create(resume: Resume): Observable<Resume> {
        const copy = this.convert(resume);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(resume: Resume): Observable<Resume> {
        const copy = this.convert(resume);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Resume> {
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
     * Convert a returned JSON object to Resume.
     */
    private convertItemFromServer(json: any): Resume {
        const entity: Resume = Object.assign(new Resume(), json);
        return entity;
    }

    /**
     * Convert a Resume to a JSON which can be sent to the server.
     */
    private convert(resume: Resume): Resume {
        const copy: Resume = Object.assign({}, resume);
        return copy;
    }
}
