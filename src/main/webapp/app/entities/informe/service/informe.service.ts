import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInforme, NewInforme } from '../informe.model';

export type PartialUpdateInforme = Partial<IInforme> & Pick<IInforme, 'id'>;

export type EntityResponseType = HttpResponse<IInforme>;
export type EntityArrayResponseType = HttpResponse<IInforme[]>;

@Injectable({ providedIn: 'root' })
export class InformeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/informes');

  create(informe: NewInforme): Observable<EntityResponseType> {
    return this.http.post<IInforme>(this.resourceUrl, informe, { observe: 'response' });
  }

  update(informe: IInforme): Observable<EntityResponseType> {
    return this.http.put<IInforme>(`${this.resourceUrl}/${this.getInformeIdentifier(informe)}`, informe, { observe: 'response' });
  }

  partialUpdate(informe: PartialUpdateInforme): Observable<EntityResponseType> {
    return this.http.patch<IInforme>(`${this.resourceUrl}/${this.getInformeIdentifier(informe)}`, informe, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInforme>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInforme[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInformeIdentifier(informe: Pick<IInforme, 'id'>): number {
    return informe.id;
  }

  compareInforme(o1: Pick<IInforme, 'id'> | null, o2: Pick<IInforme, 'id'> | null): boolean {
    return o1 && o2 ? this.getInformeIdentifier(o1) === this.getInformeIdentifier(o2) : o1 === o2;
  }

  addInformeToCollectionIfMissing<Type extends Pick<IInforme, 'id'>>(
    informeCollection: Type[],
    ...informesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const informes: Type[] = informesToCheck.filter(isPresent);
    if (informes.length > 0) {
      const informeCollectionIdentifiers = informeCollection.map(informeItem => this.getInformeIdentifier(informeItem));
      const informesToAdd = informes.filter(informeItem => {
        const informeIdentifier = this.getInformeIdentifier(informeItem);
        if (informeCollectionIdentifiers.includes(informeIdentifier)) {
          return false;
        }
        informeCollectionIdentifiers.push(informeIdentifier);
        return true;
      });
      return [...informesToAdd, ...informeCollection];
    }
    return informeCollection;
  }
}
