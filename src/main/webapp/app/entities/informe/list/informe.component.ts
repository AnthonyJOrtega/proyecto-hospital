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
import { IInforme } from '../informe.model';

import { EntityArrayResponseType, InformeService } from '../service/informe.service';
import { InformeDeleteDialogComponent } from '../delete/informe-delete-dialog.component';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { PacienteDetailComponent } from 'app/entities/paciente/detail/paciente-detail.component';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';

@Component({
  selector: 'jhi-informe',
  templateUrl: './informe.component.html',
  styleUrls: ['./informe.component.scss'],
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FilterComponent, ItemCountComponent],
})
export class InformeComponent implements OnInit {
  subscription: Subscription | null = null;
  informes = signal<IInforme[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly informeService = inject(InformeService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  constructor(
    private pacienteService: PacienteService,
    private trabajadorService: TrabajadorService,
  ) {}

  trackId = (item: IInforme): number => this.informeService.getInformeIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  delete(informe: IInforme): void {
    const modalRef = this.modalService.open(InformeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.informe = informe;
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
    this.informes.set(dataFromBody);
    this.informesFiltrados = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IInforme[] | null): IInforme[] {
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
    return this.informeService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
  //Metodo para abrir el modal de detalle del paciente
  openPacienteDetailModal(paciente: IPaciente): void {
    this.pacienteService.find(paciente.id).subscribe(response => {
      const modalRef = this.modalService.open(PacienteDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.paciente = response.body;
    });
  }
  //Metodo para abrir el modal detalle del trabajador
  openTrabajadorDetailModal(trabajador: ITrabajador): void {
    this.trabajadorService.find(trabajador.id).subscribe(response => {
      const modalRef = this.modalService.open(TrabajadorDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.trabajador = response.body;
    });
  }
  filtroId: number | null = null;
  filtroPaciente = '';
  filtroTrabajador = '';
  filtroFecha: string | null = null;

  informesFiltrados: IInforme[] = []; // Los que se muestran

  filtrarInformes(): void {
    const normalizar = (txt: string) =>
      txt
        ?.toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '') || '';

    // Paciente
    const palabrasPaciente = this.filtroPaciente
      .split(' ')
      .map(w => normalizar(w))
      .filter(Boolean);

    // Trabajador
    const palabrasTrabajador = this.filtroTrabajador
      .split(' ')
      .map(w => normalizar(w))
      .filter(Boolean);

    this.informesFiltrados = this.informes().filter(informe => {
      // ID
      const coincideId = !this.filtroId || informe.id?.toString().includes(this.filtroId?.toString());

      // Paciente
      const nombrePaciente = normalizar(informe.paciente?.nombre ?? '');
      const apellidoPaciente = normalizar(informe.paciente?.apellido ?? '');
      const dniPaciente = normalizar(informe.paciente?.dni ?? '');
      const coincidePaciente =
        palabrasPaciente.length === 0 ||
        palabrasPaciente.every(
          palabra => nombrePaciente.includes(palabra) || apellidoPaciente.includes(palabra) || dniPaciente.includes(palabra),
        );

      // Trabajador
      const nombreTrabajador = normalizar(informe.trabajador?.nombre ?? '');
      const apellidoTrabajador = normalizar(informe.trabajador?.apellido ?? '');
      const idUsuarioTrabajador = normalizar((informe.trabajador?.idUsuario ?? '').toString());
      const coincideTrabajador =
        palabrasTrabajador.length === 0 ||
        palabrasTrabajador.every(
          palabra => nombreTrabajador.includes(palabra) || apellidoTrabajador.includes(palabra) || idUsuarioTrabajador.includes(palabra),
        );

      // Fecha
      const coincideFecha = !this.filtroFecha || (informe.fecha && informe.fecha.startsWith(this.filtroFecha));

      return coincideId && coincidePaciente && coincideTrabajador && coincideFecha;
    });
  }
}
