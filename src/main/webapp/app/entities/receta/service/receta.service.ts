import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReceta, NewReceta } from '../receta.model';

export type PartialUpdateReceta = Partial<IReceta> & Pick<IReceta, 'id'>;

type RestOf<T extends IReceta | NewReceta> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

export type RestReceta = RestOf<IReceta>;

export type NewRestReceta = RestOf<NewReceta>;

export type PartialUpdateRestReceta = RestOf<PartialUpdateReceta>;

export type EntityResponseType = HttpResponse<IReceta>;
export type EntityArrayResponseType = HttpResponse<IReceta[]>;

@Injectable({ providedIn: 'root' })
export class RecetaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recetas');

  create(receta: NewReceta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receta);
    return this.http
      .post<RestReceta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(receta: IReceta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receta);
    return this.http
      .put<RestReceta>(`${this.resourceUrl}/${this.getRecetaIdentifier(receta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(receta: PartialUpdateReceta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receta);
    return this.http
      .patch<RestReceta>(`${this.resourceUrl}/${this.getRecetaIdentifier(receta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReceta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReceta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRecetaIdentifier(receta: Pick<IReceta, 'id'>): number {
    return receta.id;
  }

  compareReceta(o1: Pick<IReceta, 'id'> | null, o2: Pick<IReceta, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecetaIdentifier(o1) === this.getRecetaIdentifier(o2) : o1 === o2;
  }

  addRecetaToCollectionIfMissing<Type extends Pick<IReceta, 'id'>>(
    recetaCollection: Type[],
    ...recetasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recetas: Type[] = recetasToCheck.filter(isPresent);
    if (recetas.length > 0) {
      const recetaCollectionIdentifiers = recetaCollection.map(recetaItem => this.getRecetaIdentifier(recetaItem));
      const recetasToAdd = recetas.filter(recetaItem => {
        const recetaIdentifier = this.getRecetaIdentifier(recetaItem);
        if (recetaCollectionIdentifiers.includes(recetaIdentifier)) {
          return false;
        }
        recetaCollectionIdentifiers.push(recetaIdentifier);
        return true;
      });
      return [...recetasToAdd, ...recetaCollection];
    }
    return recetaCollection;
  }

  protected convertDateFromClient<T extends IReceta | NewReceta | PartialUpdateReceta>(receta: T): RestOf<T> {
    return {
      ...receta,
      fechaInicio: receta.fechaInicio?.format(DATE_FORMAT) ?? null,
      fechaFin: receta.fechaFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restReceta: RestReceta): IReceta {
    return {
      ...restReceta,
      fechaInicio: restReceta.fechaInicio ? dayjs(restReceta.fechaInicio) : undefined,
      fechaFin: restReceta.fechaFin ? dayjs(restReceta.fechaFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReceta>): HttpResponse<IReceta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReceta[]>): HttpResponse<IReceta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
