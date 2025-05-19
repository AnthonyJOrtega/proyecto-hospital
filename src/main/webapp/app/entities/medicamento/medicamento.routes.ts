import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MedicamentoResolve from './route/medicamento-routing-resolve.service';

const medicamentoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medicamento.component').then(m => m.MedicamentoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/medicamento-detail.component').then(m => m.MedicamentoDetailComponent),
    resolve: {
      medicamento: MedicamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/medicamento-update.component').then(m => m.MedicamentoUpdateComponent),
    resolve: {
      medicamento: MedicamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/medicamento-update.component').then(m => m.MedicamentoUpdateComponent),
    resolve: {
      medicamento: MedicamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicamentoRoute;
