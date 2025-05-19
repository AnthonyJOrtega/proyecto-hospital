import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDireccion, NewDireccion } from '../direccion.model';

export type PartialUpdateDireccion = Partial<IDireccion> & Pick<IDireccion, 'id'>;

export type EntityResponseType = HttpResponse<IDireccion>;
export type EntityArrayResponseType = HttpResponse<IDireccion[]>;

@Injectable({ providedIn: 'root' })
export class DireccionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/direccions');

  create(direccion: NewDireccion): Observable<EntityResponseType> {
    return this.http.post<IDireccion>(this.resourceUrl, direccion, { observe: 'response' });
  }

  update(direccion: IDireccion): Observable<EntityResponseType> {
    return this.http.put<IDireccion>(`${this.resourceUrl}/${this.getDireccionIdentifier(direccion)}`, direccion, { observe: 'response' });
  }

  partialUpdate(direccion: PartialUpdateDireccion): Observable<EntityResponseType> {
    return this.http.patch<IDireccion>(`${this.resourceUrl}/${this.getDireccionIdentifier(direccion)}`, direccion, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDireccion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDireccion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDireccionIdentifier(direccion: Pick<IDireccion, 'id'>): number {
    return direccion.id;
  }

  compareDireccion(o1: Pick<IDireccion, 'id'> | null, o2: Pick<IDireccion, 'id'> | null): boolean {
    return o1 && o2 ? this.getDireccionIdentifier(o1) === this.getDireccionIdentifier(o2) : o1 === o2;
  }

  addDireccionToCollectionIfMissing<Type extends Pick<IDireccion, 'id'>>(
    direccionCollection: Type[],
    ...direccionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const direccions: Type[] = direccionsToCheck.filter(isPresent);
    if (direccions.length > 0) {
      const direccionCollectionIdentifiers = direccionCollection.map(direccionItem => this.getDireccionIdentifier(direccionItem));
      const direccionsToAdd = direccions.filter(direccionItem => {
        const direccionIdentifier = this.getDireccionIdentifier(direccionItem);
        if (direccionCollectionIdentifiers.includes(direccionIdentifier)) {
          return false;
        }
        direccionCollectionIdentifiers.push(direccionIdentifier);
        return true;
      });
      return [...direccionsToAdd, ...direccionCollection];
    }
    return direccionCollection;
  }
}
