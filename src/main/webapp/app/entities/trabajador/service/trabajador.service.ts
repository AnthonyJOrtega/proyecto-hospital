import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrabajador, NewTrabajador } from '../trabajador.model';

export type PartialUpdateTrabajador = Partial<ITrabajador> & Pick<ITrabajador, 'id'>;

export type EntityResponseType = HttpResponse<ITrabajador>;
export type EntityArrayResponseType = HttpResponse<ITrabajador[]>;

@Injectable({ providedIn: 'root' })
export class TrabajadorService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trabajadors');

  create(trabajador: NewTrabajador): Observable<EntityResponseType> {
    return this.http.post<ITrabajador>(this.resourceUrl, trabajador, { observe: 'response' });
  }

  update(trabajador: ITrabajador): Observable<EntityResponseType> {
    return this.http.put<ITrabajador>(`${this.resourceUrl}/${this.getTrabajadorIdentifier(trabajador)}`, trabajador, {
      observe: 'response',
    });
  }

  partialUpdate(trabajador: PartialUpdateTrabajador): Observable<EntityResponseType> {
    return this.http.patch<ITrabajador>(`${this.resourceUrl}/${this.getTrabajadorIdentifier(trabajador)}`, trabajador, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrabajador>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrabajador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrabajadorIdentifier(trabajador: Pick<ITrabajador, 'id'>): number {
    return trabajador.id;
  }

  compareTrabajador(o1: Pick<ITrabajador, 'id'> | null, o2: Pick<ITrabajador, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrabajadorIdentifier(o1) === this.getTrabajadorIdentifier(o2) : o1 === o2;
  }

  addTrabajadorToCollectionIfMissing<Type extends Pick<ITrabajador, 'id'>>(
    trabajadorCollection: Type[],
    ...trabajadorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trabajadors: Type[] = trabajadorsToCheck.filter(isPresent);
    if (trabajadors.length > 0) {
      const trabajadorCollectionIdentifiers = trabajadorCollection.map(trabajadorItem => this.getTrabajadorIdentifier(trabajadorItem));
      const trabajadorsToAdd = trabajadors.filter(trabajadorItem => {
        const trabajadorIdentifier = this.getTrabajadorIdentifier(trabajadorItem);
        if (trabajadorCollectionIdentifiers.includes(trabajadorIdentifier)) {
          return false;
        }
        trabajadorCollectionIdentifiers.push(trabajadorIdentifier);
        return true;
      });
      return [...trabajadorsToAdd, ...trabajadorCollection];
    }
    return trabajadorCollection;
  }
}
