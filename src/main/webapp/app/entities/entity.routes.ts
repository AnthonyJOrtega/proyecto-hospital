import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'tfg2App.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'paciente',
    data: { pageTitle: 'tfg2App.paciente.home.title' },
    loadChildren: () => import('./paciente/paciente.routes'),
  },
  {
    path: 'trabajador',
    data: { pageTitle: 'tfg2App.trabajador.home.title' },
    loadChildren: () => import('./trabajador/trabajador.routes'),
  },
  {
    path: 'direccion',
    data: { pageTitle: 'tfg2App.direccion.home.title' },
    loadChildren: () => import('./direccion/direccion.routes'),
  },
  {
    path: 'cita',
    data: { pageTitle: 'tfg2App.cita.home.title' },
    loadChildren: () => import('./cita/cita.routes'),
  },
  {
    path: 'informe',
    data: { pageTitle: 'tfg2App.informe.home.title' },
    loadChildren: () => import('./informe/informe.routes'),
  },
  {
    path: 'receta',
    data: { pageTitle: 'tfg2App.receta.home.title' },
    loadChildren: () => import('./receta/receta.routes'),
  },
  {
    path: 'especialidad',
    data: { pageTitle: 'tfg2App.especialidad.home.title' },
    loadChildren: () => import('./especialidad/especialidad.routes'),
  },
  {
    path: 'enfermedad',
    data: { pageTitle: 'tfg2App.enfermedad.home.title' },
    loadChildren: () => import('./enfermedad/enfermedad.routes'),
  },
  {
    path: 'medicamento',
    data: { pageTitle: 'tfg2App.medicamento.home.title' },
    loadChildren: () => import('./medicamento/medicamento.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
