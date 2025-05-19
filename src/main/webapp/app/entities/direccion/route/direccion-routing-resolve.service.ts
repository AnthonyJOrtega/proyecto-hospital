import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDireccion } from '../direccion.model';
import { DireccionService } from '../service/direccion.service';

const direccionResolve = (route: ActivatedRouteSnapshot): Observable<null | IDireccion> => {
  const id = route.params.id;
  if (id) {
    return inject(DireccionService)
      .find(id)
      .pipe(
        mergeMap((direccion: HttpResponse<IDireccion>) => {
          if (direccion.body) {
            return of(direccion.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default direccionResolve;
