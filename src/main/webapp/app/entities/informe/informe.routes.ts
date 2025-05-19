import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InformeResolve from './route/informe-routing-resolve.service';

const informeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/informe.component').then(m => m.InformeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/informe-detail.component').then(m => m.InformeDetailComponent),
    resolve: {
      informe: InformeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/informe-update.component').then(m => m.InformeUpdateComponent),
    resolve: {
      informe: InformeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/informe-update.component').then(m => m.InformeUpdateComponent),
    resolve: {
      informe: InformeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default informeRoute;
