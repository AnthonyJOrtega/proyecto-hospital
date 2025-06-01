import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ICita } from '../cita.model';
import { CitaService } from '../service/cita.service';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-cita-list-modal',
  templateUrl: './cita-list-modal.component.html',
  imports: [CommonModule, SharedModule, RouterModule],
})
export class CitaListModalComponent implements OnInit {
  @Input() pacienteId!: number;
  citas: ICita[] = [];
  isLoading = false;

  constructor(
    public activeModal: NgbActiveModal,
    private citaService: CitaService,
  ) {}

  ngOnInit(): void {
    if (this.pacienteId) {
      this.isLoading = true;
      this.citaService.query({ 'pacienteId.in': this.pacienteId }).subscribe({
        next: res => {
          this.citas = res.body ?? [];
          this.isLoading = false;
        },
        error: () => (this.isLoading = false),
      });
    }
  }

  close(): void {
    this.activeModal.dismiss();
  }
}
