import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrabajadorResolve from './route/trabajador-routing-resolve.service';

const trabajadorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/trabajador.component').then(m => m.TrabajadorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/trabajador-detail.component').then(m => m.TrabajadorDetailComponent),
    resolve: {
      trabajador: TrabajadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/trabajador-update.component').then(m => m.TrabajadorUpdateComponent),
    resolve: {
      trabajador: TrabajadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/trabajador-update.component').then(m => m.TrabajadorUpdateComponent),
    resolve: {
      trabajador: TrabajadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trabajadorRoute;
