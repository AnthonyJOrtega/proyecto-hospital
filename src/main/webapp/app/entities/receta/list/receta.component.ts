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
import { IReceta } from '../receta.model';

import { EntityArrayResponseType, RecetaService } from '../service/receta.service';
import { RecetaDeleteDialogComponent } from '../delete/receta-delete-dialog.component';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { PacienteDetailComponent } from 'app/entities/paciente/detail/paciente-detail.component';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { jsPDF } from 'jspdf';
import html2canvas from 'html2canvas';

@Component({
  selector: 'jhi-receta',
  templateUrl: './receta.component.html',
  styleUrls: ['./receta.component.scss'],
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
export class RecetaComponent implements OnInit {
  subscription: Subscription | null = null;
  recetas = signal<IReceta[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly recetaService = inject(RecetaService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  constructor(
    // ...
    private pacienteService: PacienteService,
    private trabajadorService: TrabajadorService,
  ) {}

  trackId = (item: IReceta): number => this.recetaService.getRecetaIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  delete(receta: IReceta): void {
    const modalRef = this.modalService.open(RecetaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.receta = receta;
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
    this.recetas.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IReceta[] | null): IReceta[] {
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
    return this.recetaService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
  //MODALES PARA PACIENTES
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
  // Método para descargar una receta en PDF
  downloadRecetaAsPdf(receta: IReceta): void {
    const doc = new jsPDF();

    // Configuración de estilos
    const pageWidth = doc.internal.pageSize.getWidth();
    const margin = 10;
    const lineHeight = 15; // Espaciado entre líneas
    let y = margin;

    // Agregar el logo
    const logoUrl = '/content/images/logonegro.jpg'; // Ruta del logo
    const logoWidth = 40; // Ajuste del ancho del logo
    const logoHeight = 30; // Ajuste de la altura del logo
    doc.addImage(logoUrl, 'JPG', margin, y, logoWidth, logoHeight);

    // Encabezado con información del hospital
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(16);
    doc.text('Hospital TECH', margin + logoWidth + 5, y + 10); // Nombre del hospital
    doc.setFontSize(12);
    doc.setFont('helvetica', 'normal');
    doc.text('Dirección: Avenida Getafe Nº123, Ciudad: Madrid', margin + logoWidth + 5, y + 20);
    doc.text('Teléfono: (+34) 9002 12345', margin + logoWidth + 5, y + 30);
    y += logoHeight + 10;

    // Línea divisoria
    doc.setDrawColor(0, 0, 0);
    doc.setLineWidth(0.5);
    doc.line(margin, y, pageWidth - margin, y);
    y += lineHeight;

    // Título del PDF
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(12);
    doc.text('RECETA MÉDICA', pageWidth / 2, y, { align: 'center' });
    y += lineHeight * 1;

    // Información de los medicamentos
    doc.setFont('helvetica', 'normal');
    doc.text(`Medicamentos:`, margin, y);
    doc.setFont('helvetica', 'bold');
    doc.text(`${receta.medicamentos?.map(m => `${m.nombre} (${m.dosis})`).join(', ') || 'N/A'}`, margin + 40, y);
    y += lineHeight;

    // Instrucciones
    doc.setFont('helvetica', 'normal');
    doc.text(`Instrucciones:`, margin, y);
    doc.setFont('helvetica', 'bold');
    doc.text(`${receta.instrucciones || 'N/A'}`, margin + 40, y);
    y += lineHeight;

    // Fechas
    doc.setFont('helvetica', 'normal');
    doc.text(`Fecha de Inicio:`, margin, y);
    doc.setFont('helvetica', 'bold');
    doc.text(`${receta.fechaInicio || 'N/A'}`, margin + 40, y);
    y += lineHeight;

    doc.setFont('helvetica', 'normal');
    doc.text(`Fecha de Fin:`, margin, y);
    doc.setFont('helvetica', 'bold');
    doc.text(`${receta.fechaFin || 'N/A'}`, margin + 40, y);
    y += lineHeight * 1;

    // NOMBRE DEL PACIENTE
    doc.setFont('helvetica', 'normal');
    doc.text(`Nombre del Paciente:`, margin, y);
    doc.setFont('helvetica', 'bold');
    doc.text(receta.paciente ? `${receta.paciente.nombre} ${receta.paciente.apellido}` : 'N/A', margin + 44, y);
    y += lineHeight;
    // DNI DEL PACIENTE
    doc.setFont('helvetica', 'normal');
    doc.text(`DNI del Paciente:`, margin, y);
    doc.setFont('helvetica', 'bold');
    doc.text(receta.paciente ? `${receta.paciente.dni}` : 'N/A', margin + 44, y);
    y += lineHeight;

    // Texto "Nombre del Profesional:" en normal
    doc.setFont('helvetica', 'normal');
    doc.text('Nombre Profesional:', margin, y);

    // Nombre real en negrita, justo a la derecha del label
    if (receta.trabajador) {
      doc.setFont('helvetica', 'bold');
      doc.text(
        `${receta.trabajador.nombre} ${receta.trabajador.apellido}`,
        margin + 44, // Ajusta este valor para alinear a la derecha del label
        y,
      );
    } else {
      doc.setFont('helvetica', 'bold');
      doc.text('N/A', margin + 50, y);
    }
    y += lineHeight;
    //ID DEL PROFESIONAL
    doc.setFont('helvetica', 'normal');
    doc.text(`ID del Profesional:`, margin, y);
    doc.setFont('helvetica', 'bold');
    doc.text(`${receta.trabajador?.idUsuario || 'N/A'}`, margin + 44, y);
    y += lineHeight * 1;
    // Especialidad del profesional
    doc.setFont('helvetica', 'normal');
    doc.text('Especialidad:', margin, y);

    const especialidades =
      receta.trabajador?.especialidads && Array.isArray(receta.trabajador.especialidads)
        ? receta.trabajador.especialidads
            .map((e: any) => e.nombre)
            .filter(Boolean)
            .join(', ')
        : '';

    doc.setFont('helvetica', 'bold');
    doc.text(
      especialidades || 'N/A',
      margin + 30, // Alineado con el nombre
      y,
    );
    y += lineHeight;

    // Línea divisoria
    doc.setDrawColor(0, 0, 0);
    doc.setLineWidth(0.5);
    doc.line(margin, y, pageWidth - margin, y);
    y += lineHeight;

    // Pie de página
    doc.setFont('helvetica', 'italic');
    doc.setFontSize(10);
    doc.text('Esta receta es válida únicamente para el uso indicado.', margin, y);
    y += lineHeight;

    doc.setFont('helvetica', 'normal');
    doc.text(`Generado el: ${new Date().toLocaleDateString()}`, margin, y);
    y += lineHeight - 15;

    // Zona de firma del paciente
    doc.setDrawColor(0, 0, 0);
    doc.setLineWidth(0.5);
    doc.line(margin, y + 5, pageWidth / 2 - margin, y + 5); // Línea para la firma
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(10);
    doc.text(' ', margin, y + 15);
    // Texto "Nombre del Paciente:" en normal
    doc.setFont('helvetica', 'normal');
    doc.text('Nombre del Paciente:', margin, y + 15);

    // Nombre real en negrita, justo a la derecha
    if (receta.paciente) {
      doc.setFont('helvetica', 'bold');
      doc.text(
        `${receta.paciente.nombre} ${receta.paciente.apellido}`,
        margin + 40, // Ajusta el +40 para alinear a la derecha del label
        y + 15,
      );
    } else {
      doc.setFont('helvetica', 'bold');
      doc.text('N/A', margin + 50, y + 15);
    }
    doc.line(margin, y + 20, pageWidth / 2 - margin, y + 20); // Línea para el nombre
    y += lineHeight * 2;
    // Zona de firma del paciente
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(12);
    doc.text('Firma del Paciente:', margin, y);
    // Línea para la firma del paciente
    doc.setDrawColor(0, 0, 0);
    doc.setLineWidth(0.5);
    doc.line(pageWidth / 2 + margin, y - lineHeight * 2 + 5, pageWidth - margin, y - lineHeight * 2 + 5); // Línea para la firma
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(10);
    doc.text(' ', margin, y + 15);
    // Texto "Nombre del Trabajador:" en normal
    doc.setFont('helvetica', 'normal');
    doc.text('Nombre del Trabajador:', pageWidth / 2 + margin, y - lineHeight + 1);
    // Nombre real en negrita, justo a la derecha
    if (receta.trabajador) {
      doc.setFont('helvetica', 'bold');
      doc.text(
        `${receta.trabajador.nombre} ${receta.trabajador.apellido}`,
        pageWidth / 2 + margin + 40, // Ajusta el +50 para alinear a la derecha del label
        y - lineHeight + 1,
      );
    } else {
      doc.setFont('helvetica', 'bold');
      doc.text('N/A', pageWidth / 2 + margin + 50, y - lineHeight + 5);
    }
    doc.line(pageWidth / 2 + margin, y - lineHeight + 6, pageWidth - margin, y - lineHeight + 6); // Línea para el nombre
    y += lineHeight * 2;
    // Zona de firma del trabajador
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(12);
    doc.text('Firma del Trabajador:', pageWidth / 2 + margin, y - lineHeight * 2);
    // Guardar el PDF
    doc.save(`receta-${receta.id}.pdf`);
  }

  // Alternativa usando html2canvas para capturar una tabla o sección HTML
  downloadRecetaHtmlAsPdf(recetaId: number): void {
    const element = document.getElementById(`receta-${recetaId}`);
    if (element) {
      html2canvas(element).then(canvas => {
        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF();
        const imgProps = pdf.getImageProperties(imgData);
        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;

        pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
        pdf.save(`receta-${recetaId}.pdf`);
      });
    }
  }
}
