import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnfermedad } from '../enfermedad.model';
import { EnfermedadService } from '../service/enfermedad.service';

const enfermedadResolve = (route: ActivatedRouteSnapshot): Observable<null | IEnfermedad> => {
  const id = route.params.id;
  if (id) {
    return inject(EnfermedadService)
      .find(id)
      .pipe(
        mergeMap((enfermedad: HttpResponse<IEnfermedad>) => {
          if (enfermedad.body) {
            return of(enfermedad.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default enfermedadResolve;
