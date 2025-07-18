import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { ICita } from '../cita.model';

import { CitaService, EntityArrayResponseType } from '../service/cita.service';
import { CitaDeleteDialogComponent } from '../delete/cita-delete-dialog.component';
import { CitaListModalComponent } from './cita-list-modal.component';
import { PacienteDetailComponent } from 'app/entities/paciente/detail/paciente-detail.component';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

@Component({
  selector: 'jhi-cita',
  templateUrl: './cita.component.html',
  styleUrls: ['./cita.component.scss'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    FormatMediumDatePipe,
    FilterComponent,
    ItemCountComponent,
  ],
})
export class CitaComponent implements OnInit {
  subscription: Subscription | null = null;
  citas = signal<ICita[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly citaService = inject(CitaService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  constructor(
    // ...otros servicios...
    private pacienteService: PacienteService,
  ) {}

  trackId = (item: ICita): number => this.citaService.getCitaIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  delete(cita: ICita): void {
    const modalRef = this.modalService.open(CitaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cita = cita;
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
    this.citas.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: ICita[] | null): ICita[] {
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
    return this.citaService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
  openPacienteDetailModal(paciente: IPaciente): void {
    this.pacienteService.find(paciente.id).subscribe(response => {
      const modalRef = this.modalService.open(PacienteDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.paciente = response.body;
    });
  }
  filtroIdCita = '';
  filtroPaciente = '';
  filtroFecha = '';
  filtroEstado = '';
  citasFiltradas: ICita[] = [];

  filtrarCitas(): void {
    const normalizar = (txt: string) =>
      txt
        ?.toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '') || '';

    const palabrasPaciente = this.filtroPaciente
      .split(' ')
      .map(w => normalizar(w))
      .filter(Boolean);

    this.citasFiltradas = this.citas().filter(cita => {
      // Filtro por ID de cita
      const coincideId = !this.filtroIdCita || cita.id?.toString().includes(this.filtroIdCita);

      // Filtro por paciente (nombre o apellido)
      const nombre = normalizar(cita.paciente?.nombre || '');
      const apellido = normalizar(cita.paciente?.apellido || '');
      const coincidePaciente =
        palabrasPaciente.length === 0 || palabrasPaciente.every(palabra => nombre.includes(palabra) || apellido.includes(palabra));

      // Filtro por fecha (solo compara la parte de la fecha)
      const coincideFecha =
        !this.filtroFecha || (cita.fechaCreacion && cita.fechaCreacion.format('YYYY-MM-DD').startsWith(this.filtroFecha));

      // Filtro por estado
      const coincideEstado = !this.filtroEstado || cita.estadoCita === this.filtroEstado;

      return coincideId && coincidePaciente && coincideFecha && coincideEstado;
    });
  }
}
