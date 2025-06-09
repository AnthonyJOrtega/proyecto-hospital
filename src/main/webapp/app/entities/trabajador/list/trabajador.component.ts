import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { ITrabajador } from '../trabajador.model';

import { EntityArrayResponseType, TrabajadorService } from '../service/trabajador.service';
import { TrabajadorDeleteDialogComponent } from '../delete/trabajador-delete-dialog.component';
import { InformeListModalTrabajadorComponent } from 'app/entities/informe/list/modal-trabajador-mostrarInforme/informe-list-modal-trabajador.component';
import { RecetaListModalTrabajadorComponent } from 'app/entities/receta/list/modal-trabajador-mostrarReceta/receta-list-modal-trabajador.component';
import { TrabajadorDetailComponent } from '../detail/trabajador-detail.component';

@Component({
  selector: 'jhi-trabajador',
  templateUrl: './trabajador.component.html',
  styleUrls: ['./trabajador.component.scss'],
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FilterComponent, ItemCountComponent],
})
export class TrabajadorComponent implements OnInit {
  subscription: Subscription | null = null;
  trabajadors = signal<ITrabajador[]>([]);
  isLoading = false;
  filtroTrabajador = '';
  filtroIdUsuario = '';
  trabajadoresFiltrados: ITrabajador[] = [];
  filtroDisponibilidad = '';

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly trabajadorService = inject(TrabajadorService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: ITrabajador): number => this.trabajadorService.getTrabajadorIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  delete(trabajador: ITrabajador): void {
    const modalRef = this.modalService.open(TrabajadorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trabajador = trabajador;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.trabajadors.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: ITrabajador[] | null): ITrabajador[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page, filters } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    filters.filterOptions.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    return this.trabajadorService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
  // Modales para mostrar detalles de un trabajador
  openDetailModal(trabajador: ITrabajador): void {
    this.trabajadorService.find(trabajador.id).subscribe(response => {
      const modalRef = this.modalService.open(TrabajadorDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.trabajador = response.body;
    });
  }
  //Modales para mostrar informes y recetas de un trabajador
  openInformesModal(trabajador: ITrabajador): void {
    const modalRef = this.modalService.open(InformeListModalTrabajadorComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trabajadorId = trabajador.id;
  }
  openRecetasModal(trabajador: ITrabajador): void {
    const modalRef = this.modalService.open(RecetaListModalTrabajadorComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trabajadorId = trabajador.id;
  }

  //METODO PARA FILTRAR TRABAJADORES POR NOMBRE E ID DE USUARIO
  filtrarTrabajadores(): void {
    const normalizar = (txt: string) =>
      txt
        .toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '');

    const palabras = this.filtroTrabajador
      .split(' ')
      .map(w => normalizar(w))
      .filter(Boolean);

    const filtroIdUsuarioNorm = this.filtroIdUsuario.trim().toLowerCase();

    this.trabajadoresFiltrados = this.trabajadors().filter(trabajador => {
      const nombre = normalizar(trabajador.nombre || '');
      const apellido = normalizar(trabajador.apellido || '');
      const idUsuario = (trabajador.idUsuario || '').toString().toLowerCase();

      const coincideNombreApellido =
        palabras.length === 0 || palabras.every(palabra => nombre.includes(palabra) || apellido.includes(palabra));

      const coincideIdUsuario = !filtroIdUsuarioNorm || idUsuario.includes(filtroIdUsuarioNorm);

      // Filtro de disponibilidad
      const coincideDisponibilidad = this.filtroDisponibilidad === '' || String(trabajador.disponibilidad) === this.filtroDisponibilidad;

      // Deben cumplirse todos los filtros activos
      return coincideNombreApellido && coincideIdUsuario && coincideDisponibilidad;
    });
  }
}
