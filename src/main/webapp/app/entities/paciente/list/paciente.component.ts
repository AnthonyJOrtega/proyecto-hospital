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
import { IPaciente } from '../paciente.model';

import { EntityArrayResponseType, PacienteService } from '../service/paciente.service';
import { PacienteDeleteDialogComponent } from '../delete/paciente-delete-dialog.component';
import { PacienteDetailComponent } from '../detail/paciente-detail.component';
import { CitaListModalComponent } from 'app/entities/cita/list/cita-list-modal.component';
import { InformeListModalComponent } from 'app/entities/informe/list/modal-paciente-mostrarInforme/informe-list-modal.component'; // Ajusta la ruta si es necesario
import { RecetaListModalComponent } from 'app/entities/receta/list/modal-paciente-verReceta/receta-list-modal.component';

@Component({
  selector: 'jhi-paciente',
  templateUrl: './paciente.component.html',
  styleUrls: ['./paciente.component.scss'],
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
export class PacienteComponent implements OnInit {
  subscription: Subscription | null = null;
  pacientes = signal<IPaciente[]>([]);
  isLoading = false;
  filtroPaciente = '';
  pacientesFiltrados: IPaciente[] = [];
  filtroDni = '';

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly pacienteService = inject(PacienteService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IPaciente): number => this.pacienteService.getPacienteIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  delete(paciente: IPaciente): void {
    const modalRef = this.modalService.open(PacienteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paciente = paciente;
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
        this.filtrarPacientes();
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
    this.pacientes.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IPaciente[] | null): IPaciente[] {
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
    return this.pacienteService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
  openDetailModal(paciente: IPaciente): void {
    const modalRef = this.modalService.open(PacienteDetailComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paciente = paciente;
  }

  openCitasModal(paciente: IPaciente): void {
    const modalRef = this.modalService.open(CitaListModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pacienteId = paciente.id;
  }

  openInformesModal(paciente: IPaciente): void {
    const modalRef = this.modalService.open(InformeListModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pacienteId = paciente.id;
  }
  openRecetasModal(paciente: IPaciente): void {
    const modalRef = this.modalService.open(RecetaListModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pacienteId = paciente.id;
  }
  //FILTRO PARA PACIENTE Y DNI
  filtrarPacientes(): void {
    const normalizar = (txt: string) =>
      txt
        .toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '');

    const palabras = this.filtroPaciente
      .split(' ')
      .map(w => normalizar(w))
      .filter(Boolean);

    const filtroDniNorm = this.filtroDni.trim().toLowerCase();

    this.pacientesFiltrados = this.pacientes().filter(paciente => {
      const nombre = normalizar(paciente.nombre || '');
      const apellido = normalizar(paciente.apellido || '');
      const dni = (paciente.dni || '').toLowerCase();

      const coincideNombreApellido =
        palabras.length === 0 || palabras.every(palabra => nombre.includes(palabra) || apellido.includes(palabra));

      const coincideDni = !filtroDniNorm || dni.includes(filtroDniNorm);

      // Deben cumplirse ambos filtros si están activos
      return coincideNombreApellido && coincideDni;
    });
  }
}
