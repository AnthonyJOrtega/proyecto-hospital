import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DireccionResolve from './route/direccion-routing-resolve.service';

const direccionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/direccion.component').then(m => m.DireccionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/direccion-detail.component').then(m => m.DireccionDetailComponent),
    resolve: {
      direccion: DireccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/direccion-update.component').then(m => m.DireccionUpdateComponent),
    resolve: {
      direccion: DireccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/direccion-update.component').then(m => m.DireccionUpdateComponent),
    resolve: {
      direccion: DireccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default direccionRoute;
