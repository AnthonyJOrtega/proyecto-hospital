import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrabajador } from '../trabajador.model';
import { TrabajadorService } from '../service/trabajador.service';

const trabajadorResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrabajador> => {
  const id = route.params.id;
  if (id) {
    return inject(TrabajadorService)
      .find(id)
      .pipe(
        mergeMap((trabajador: HttpResponse<ITrabajador>) => {
          if (trabajador.body) {
            return of(trabajador.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trabajadorResolve;
