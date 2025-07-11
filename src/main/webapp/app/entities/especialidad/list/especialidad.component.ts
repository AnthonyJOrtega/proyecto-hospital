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
import { IEspecialidad } from '../especialidad.model';

import { EntityArrayResponseType, EspecialidadService } from '../service/especialidad.service';
import { EspecialidadDeleteDialogComponent } from '../delete/especialidad-delete-dialog.component';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';

@Component({
  selector: 'jhi-especialidad',
  templateUrl: './especialidad.component.html',
  styleUrls: ['./especialidad.component.scss'],
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FilterComponent, ItemCountComponent],
})
export class EspecialidadComponent implements OnInit {
  subscription: Subscription | null = null;
  especialidads = signal<IEspecialidad[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  filtroNombreEspecialidad = '';
  especialidadsFiltradas: IEspecialidad[] = [];

  public readonly router = inject(Router);
  protected readonly especialidadService = inject(EspecialidadService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected readonly trabajadorService = inject(TrabajadorService);

  trackId = (item: IEspecialidad): number => this.especialidadService.getEspecialidadIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  delete(especialidad: IEspecialidad): void {
    const modalRef = this.modalService.open(EspecialidadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.especialidad = especialidad;
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
        this.filtrarEspecialidadesPorNombre();
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
    this.especialidads.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IEspecialidad[] | null): IEspecialidad[] {
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
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    filters.filterOptions.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    return this.especialidadService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  //Metodo para abrir el modal del trabjaador segun id de usuario
  openTrabajadorDetailModal(trabajador: ITrabajador): void {
    this.trabajadorService.find(trabajador.id).subscribe(response => {
      const modalRef = this.modalService.open(TrabajadorDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.trabajador = response.body;
    });
  }
  //Metodo para filtrar las especialidades por nombre
  filtrarEspecialidadesPorNombre(): void {
    const filtro = this.filtroNombreEspecialidad
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
    if (!filtro) {
      this.especialidadsFiltradas = this.especialidads();
      return;
    }
    this.especialidadsFiltradas = this.especialidads().filter(especialidad =>
      especialidad.nombre
        ?.toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .includes(filtro),
    );
  }
}
