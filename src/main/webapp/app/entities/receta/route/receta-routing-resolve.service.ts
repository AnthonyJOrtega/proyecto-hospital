import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReceta } from '../receta.model';
import { RecetaService } from '../service/receta.service';

const recetaResolve = (route: ActivatedRouteSnapshot): Observable<null | IReceta> => {
  const id = route.params.id;
  if (id) {
    return inject(RecetaService)
      .find(id)
      .pipe(
        mergeMap((receta: HttpResponse<IReceta>) => {
          if (receta.body) {
            return of(receta.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default recetaResolve;
