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
    editable: false,
    selectable: true,

    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,dayGridWeek,dayGridDay',
    },
    dateClick: this.onDateClick.bind(this),
    eventClick: this.onEventClick.bind(this),
    eventMouseEnter: this.onEventMouseEnter.bind(this), // Agrega el evento para el tooltip
    eventMouseLeave: this.onEventMouseLeave.bind(this), // Limpia el tooltip al salir
  };
  citasOriginales: any[] = [];
  filterPaciente: string = '';
  filterTrabajador: string = '';
  trabajadorsSharedCollection: any;
  pacientesSharedCollection: IPaciente[] = [];

  mostrarMensajeFechaPasada = false;

  constructor(
    private modalService: NgbModal,
    private http: HttpClient,
    private trabajadorService: TrabajadorService,
    private pacienteService: PacienteService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.cargarCitasModal();
    this.cargarTrabajadores();
    this.cargarPacientes();
  }
  cargarTrabajadores(): void {
    this.trabajadorService.query({ size: 10000 }).subscribe((res: { body: ITrabajador[] | null }) => {
      // Solo médicos
      this.trabajadorsSharedCollection = (res.body ?? []).filter(t => t.puesto === 'MEDICO');
    });
  }
  cargarPacientes(): void {
    this.pacienteService.query({ size: 10000 }).subscribe((res: { body: IPaciente[] | null }) => {
      this.pacientesSharedCollection = res.body ?? [];
    });
  }

  onDateClick(arg: any): void {
    const fecha = arg.dateStr;
    const hoy = new Date().toISOString().slice(0, 10);
    if (fecha < hoy) {
      this.mostrarMensajeFechaPasada = true;
      // Hace visible el mensaje de fecha pasada durante 2 segundos
      setTimeout(() => (this.mostrarMensajeFechaPasada = false), 2000);

      return;
    }
    this.mostrarMensajeFechaPasada = false; // Oculta el mensaje si la fecha es válida
    this.abrirModalCita(undefined, fecha);
  }

  abrirModalCita(citaId?: number, fecha?: string): void {
    this.mostrarMensajeFechaPasada = false; // Oculta el mensaje al abrir el modal manualmente
    const modalRef = this.modalService.open(CitaUpdateComponent, {
      size: 'lg',
      backdrop: 'static',
    });

    if (citaId) {
      modalRef.componentInstance.citaId = citaId;
    } else if (fecha) {
      modalRef.componentInstance.fecha = fecha;
    }

    modalRef.closed.subscribe(() => {
      this.cargarCitasModal(); // Refresca eventos tras cerrar modal
    });
  }

  filtrarCitas(): void {
    const citasFiltradas = this.citasOriginales.filter(
      (cita: { paciente: { nombre: any; apellido: any }; trabajadors: { nombre: any; apellido: any }[] }) => {
        const coincidePaciente = this.filterPaciente
          ? `${cita.paciente?.nombre} ${cita.paciente?.apellido}`.toLowerCase().includes(this.filterPaciente.toLowerCase())
          : true;
        const coincideTrabajador = this.filterTrabajador
          ? cita.trabajadors?.some((trabajador: { nombre: any; apellido: any }) =>
              `${trabajador.nombre} ${trabajador.apellido}`.toLowerCase().includes(this.filterTrabajador.toLowerCase()),
            )
          : true;
        return coincidePaciente && coincideTrabajador;
      },
    );

    this.calendarOptions.events = this.mapearCitas(citasFiltradas);
  }

  private mapearCitas(citas: any[]): any[] {
    return citas.map(cita => {
      let color = '';
      let text = '';
      switch (cita.estadoCita) {
        case 'FINALIZADO':
          color = '#28a745'; // verde
          break;
        case 'PENDIENTE':
          color = '#ffc107'; // amarillo
          break;
        case 'CANCELADO':
          color = '#dc3545'; // rojo
          break;
        default:
          color = '#ffffff'; // gris
          text = '#000000'; // blanco
      }

      // Construir el título con el nombre del paciente y los trabajadores
      const pacienteNombre = cita.paciente ? `${cita.paciente.nombre} ${cita.paciente.apellido}` : 'Paciente no asignado';
      const trabajadoresNombres =
        cita.trabajadors && cita.trabajadors.length > 0
          ? cita.trabajadors.map((trabajador: any) => `${trabajador.nombre} ${trabajador.apellido}`).join(', ')
          : 'Trabajador no asignado';

      // Información adicional para el tooltip
      const tooltipInfo = `
        Paciente: ${pacienteNombre}
        Trabajador(es): ${trabajadoresNombres}
      `;

      return {
        title: cita.paciente ? `Cita con ${cita.paciente.nombre} ${cita.paciente.apellido}` : 'Cita sin paciente asignado',
        date: cita.fechaCreacion,
        backgroundColor: color,
        borderColor: color,
        textColor: text,
        extendedProps: {
          id: cita.id,
          tooltip: tooltipInfo.trim(), // Información para el tooltip
        },
      };
    });
  }

  cargarCitasModal(): void {
    this.http.get<any[]>('/api/citas?size=9999').subscribe(citas => {
      this.citasOriginales = citas; // Guarda las citas originales
      this.calendarOptions.events = this.mapearCitas(citas);
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

    // Guarda la referencia para poder quitar el listener después
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
    console.log('CitaId para modal:', citaId);
    if (citaId) {
      const modalRef = this.modalService.open(CitaDetailComponent, {
        size: 'lg',
        backdrop: 'static',
      });
      modalRef.componentInstance.citaId = citaId;
    }
  }
}
