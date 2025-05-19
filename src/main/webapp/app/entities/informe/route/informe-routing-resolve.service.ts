import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInforme } from '../informe.model';
import { InformeService } from '../service/informe.service';

const informeResolve = (route: ActivatedRouteSnapshot): Observable<null | IInforme> => {
  const id = route.params.id;
  if (id) {
    return inject(InformeService)
      .find(id)
      .pipe(
        mergeMap((informe: HttpResponse<IInforme>) => {
          if (informe.body) {
            return of(informe.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default informeResolve;
