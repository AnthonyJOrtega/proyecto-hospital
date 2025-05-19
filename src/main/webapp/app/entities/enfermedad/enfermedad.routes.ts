import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EnfermedadResolve from './route/enfermedad-routing-resolve.service';

const enfermedadRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/enfermedad.component').then(m => m.EnfermedadComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/enfermedad-detail.component').then(m => m.EnfermedadDetailComponent),
    resolve: {
      enfermedad: EnfermedadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/enfermedad-update.component').then(m => m.EnfermedadUpdateComponent),
    resolve: {
      enfermedad: EnfermedadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/enfermedad-update.component').then(m => m.EnfermedadUpdateComponent),
    resolve: {
      enfermedad: EnfermedadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default enfermedadRoute;
