import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnfermedad, NewEnfermedad } from '../enfermedad.model';

export type PartialUpdateEnfermedad = Partial<IEnfermedad> & Pick<IEnfermedad, 'id'>;

export type EntityResponseType = HttpResponse<IEnfermedad>;
export type EntityArrayResponseType = HttpResponse<IEnfermedad[]>;

@Injectable({ providedIn: 'root' })
export class EnfermedadService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/enfermedads');

  create(enfermedad: NewEnfermedad): Observable<EntityResponseType> {
    return this.http.post<IEnfermedad>(this.resourceUrl, enfermedad, { observe: 'response' });
  }

  update(enfermedad: IEnfermedad): Observable<EntityResponseType> {
    return this.http.put<IEnfermedad>(`${this.resourceUrl}/${this.getEnfermedadIdentifier(enfermedad)}`, enfermedad, {
      observe: 'response',
    });
  }

  partialUpdate(enfermedad: PartialUpdateEnfermedad): Observable<EntityResponseType> {
    return this.http.patch<IEnfermedad>(`${this.resourceUrl}/${this.getEnfermedadIdentifier(enfermedad)}`, enfermedad, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnfermedad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnfermedad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEnfermedadIdentifier(enfermedad: Pick<IEnfermedad, 'id'>): number {
    return enfermedad.id;
  }

  compareEnfermedad(o1: Pick<IEnfermedad, 'id'> | null, o2: Pick<IEnfermedad, 'id'> | null): boolean {
    return o1 && o2 ? this.getEnfermedadIdentifier(o1) === this.getEnfermedadIdentifier(o2) : o1 === o2;
  }

  addEnfermedadToCollectionIfMissing<Type extends Pick<IEnfermedad, 'id'>>(
    enfermedadCollection: Type[],
    ...enfermedadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const enfermedads: Type[] = enfermedadsToCheck.filter(isPresent);
    if (enfermedads.length > 0) {
      const enfermedadCollectionIdentifiers = enfermedadCollection.map(enfermedadItem => this.getEnfermedadIdentifier(enfermedadItem));
      const enfermedadsToAdd = enfermedads.filter(enfermedadItem => {
        const enfermedadIdentifier = this.getEnfermedadIdentifier(enfermedadItem);
        if (enfermedadCollectionIdentifiers.includes(enfermedadIdentifier)) {
          return false;
        }
        enfermedadCollectionIdentifiers.push(enfermedadIdentifier);
        return true;
      });
      return [...enfermedadsToAdd, ...enfermedadCollection];
    }
    return enfermedadCollection;
  }
}
