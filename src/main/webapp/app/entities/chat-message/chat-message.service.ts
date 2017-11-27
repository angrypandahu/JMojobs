import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ChatMessage } from './chat-message.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ChatMessageService {

    private resourceUrl = SERVER_API_URL + 'api/chat-messages';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/chat-messages';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(chatMessage: ChatMessage): Observable<ChatMessage> {
        const copy = this.convert(chatMessage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(chatMessage: ChatMessage): Observable<ChatMessage> {
        const copy = this.convert(chatMessage);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ChatMessage> {
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
     * Convert a returned JSON object to ChatMessage.
     */
    private convertItemFromServer(json: any): ChatMessage {
        const entity: ChatMessage = Object.assign(new ChatMessage(), json);
        entity.sendTime = this.dateUtils
            .convertLocalDateFromServer(json.sendTime);
        return entity;
    }

    /**
     * Convert a ChatMessage to a JSON which can be sent to the server.
     */
    private convert(chatMessage: ChatMessage): ChatMessage {
        const copy: ChatMessage = Object.assign({}, chatMessage);
        copy.sendTime = this.dateUtils
            .convertLocalDateToServer(chatMessage.sendTime);
        return copy;
    }
}
