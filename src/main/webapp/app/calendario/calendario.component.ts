import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CitaUpdateComponent } from '../entities/cita/update/cita-update.component';
import { CitaDetailComponent } from 'app/entities/cita/detail/cita-detail.component';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-calendario',
  standalone: true,
  templateUrl: './calendario.component.html',
  styleUrls: ['./calendario.component.css'],
  imports: [CommonModule, FullCalendarModule, FormsModule, TranslateModule],
})
export class CalendarioComponent implements OnInit {
  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    locale: 'es',
    buttonText: {
      today: 'Hoy',
      month: 'Mes',
      week: 'Semana',
      day: 'Día',
    },
    events: [],
    eventContent: function (arg) {
      // arg.event.title puede contener HTML
      return { html: arg.event.title };
    },
    editable: false,
    selectable: true,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,dayGridWeek,dayGridDay',
    },
    dateClick: this.onDateClick.bind(this),
    eventClick: this.onEventClick.bind(this),
    eventMouseEnter: this.onEventMouseEnter.bind(this),
    eventMouseLeave: this.onEventMouseLeave.bind(this),
  };
  citasOriginales: any[] = [];
  mostrarMensajeFechaPasada = false;

  constructor(
    private modalService: NgbModal,
    private http: HttpClient,
    private trabajadorService: TrabajadorService,
    private pacienteService: PacienteService,
    private router: Router,
  ) {}

  filterPaciente: string = '';
  filterTrabajador: string = '';

  ngOnInit(): void {
    this.cargarCitasModal();
  }

  onDateClick(arg: any): void {
    const fecha = arg.dateStr;
    const hoy = new Date().toISOString().slice(0, 10);
    if (fecha < hoy) {
      this.mostrarMensajeFechaPasada = true;
      setTimeout(() => (this.mostrarMensajeFechaPasada = false), 2000);
      return;
    }
    this.mostrarMensajeFechaPasada = false;
    this.abrirModalCita(undefined, fecha);
  }

  abrirModalCita(citaId?: number, fecha?: string): void {
    this.mostrarMensajeFechaPasada = false;
    const modalRef = this.modalService.open(CitaUpdateComponent, {
      size: 'lg',
      backdrop: 'static',
    });

    if (citaId) {
      modalRef.componentInstance.citaId = citaId;
    } else if (fecha) {
      modalRef.componentInstance.fecha = fecha;
    }

    // Solo refresca si el modal se cierra con 'saved'
    modalRef.closed.subscribe(result => {
      if (result === 'saved') {
        this.cargarCitasModal(); // Refresca el calendario
      }
    });
  }

  cargarCitasModal(): void {
    this.http.get<any[]>('/api/citas?size=9999').subscribe(citas => {
      this.citasOriginales = citas;
      this.aplicarFiltros();
      this.calendarOptions.events = this.mapearCitas(citas);
    });
  }

  private mapearCitas(citas: any[]): any[] {
    return citas.map(cita => {
      let color = '';
      let text = '';
      switch (cita.estadoCita) {
        case 'FINALIZADO':
          color = '#28a745';
          break;
        case 'PENDIENTE':
          color = '#ffc107';
          break;
        case 'CANCELADO':
          color = '#dc3545';
          break;
        default:
          color = '#ffffff';
          text = '#000000';
      }
      const pacienteNombre = cita.paciente ? `${cita.paciente.nombre} ${cita.paciente.apellido}` : 'Paciente no asignado';
      const trabajadoresNombres =
        cita.trabajadors && cita.trabajadors.length > 0
          ? cita.trabajadors.map((trabajador: any) => `${trabajador.nombre} ${trabajador.apellido}`).join(', ')
          : 'Trabajador no asignado';
      const tooltipInfo = `
        Paciente: ${pacienteNombre}
        Trabajador(es): ${trabajadoresNombres}
      `;
      return {
        title: cita.paciente
          ? `Cita con ${cita.paciente.nombre} ${cita.paciente.apellido} <b>(${cita.horaCreacion})</b>`
          : 'Cita sin paciente asignado',
        date: cita.fechaCreacion,
        backgroundColor: color,
        borderColor: color,
        textColor: text,
        extendedProps: {
          id: cita.id,
          tooltip: tooltipInfo.trim(),
        },
      };
    });
  }

  private tooltipListener?: (event: MouseEvent) => void;

  onEventMouseEnter(info: any): void {
    const tooltip = document.createElement('div');
    tooltip.id = 'event-tooltip';
    tooltip.style.position = 'absolute';
    tooltip.style.backgroundColor = '#fff';
    tooltip.style.border = '1px solid #ccc';
    tooltip.style.padding = '10px';
    tooltip.style.borderRadius = '5px';
    tooltip.style.boxShadow = '0 2px 5px rgba(0, 0, 0, 0.2)';
    tooltip.style.zIndex = '1000';
    tooltip.innerText = info.event.extendedProps.tooltip;
    document.body.appendChild(tooltip);
    this.tooltipListener = function moveTooltip(event: MouseEvent) {
      tooltip.style.top = `${event.pageY + 10}px`;
      tooltip.style.left = `${event.pageX + 10}px`;
    };
    document.addEventListener('mousemove', this.tooltipListener);
  }

  onEventMouseLeave(): void {
    const tooltip = document.getElementById('event-tooltip');
    if (tooltip) {
      tooltip.remove();
    }
    if (this.tooltipListener) {
      document.removeEventListener('mousemove', this.tooltipListener);
      this.tooltipListener = undefined;
    }
  }

  onEventClick(arg: any): void {
    this.onEventMouseLeave();
    const citaId = arg.event.extendedProps?.id;
    if (citaId) {
      const modalRef = this.modalService.open(CitaDetailComponent, {
        size: 'lg',
        backdrop: 'static',
      });
      modalRef.componentInstance.citaId = citaId;

      // Refresca el calendario si se elimina la cita
      modalRef.closed.subscribe(result => {
        if (result === 'deleted') {
          this.cargarCitasModal();
        }
      });
    }
  }
  aplicarFiltros(): void {
    let citasFiltradas = this.citasOriginales;

    // Función para normalizar texto (quita acentos y pasa a minúsculas)
    const normalizar = (texto: string) =>
      texto
        ? texto
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .toLowerCase()
        : '';

    if (this.filterPaciente.trim()) {
      const filtro = normalizar(this.filterPaciente.trim());
      citasFiltradas = citasFiltradas.filter(
        cita =>
          cita.paciente &&
          (normalizar(`${cita.paciente.nombre} ${cita.paciente.apellido}`).includes(filtro) ||
            (cita.paciente.dni && normalizar(cita.paciente.dni).includes(filtro))),
      );
    }

    if (this.filterTrabajador.trim()) {
      const filtro = normalizar(this.filterTrabajador.trim());
      citasFiltradas = citasFiltradas.filter(
        cita =>
          cita.trabajadors &&
          cita.trabajadors.some((trabajador: any) => normalizar(`${trabajador.nombre} ${trabajador.apellido}`).includes(filtro)),
      );
    }

    this.calendarOptions.events = this.mapearCitas(citasFiltradas);
  }
}
