import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IInforme } from '../../informe.model';
import { InformeService } from '../../service/informe.service';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'jhi-informe-list-modal',
  templateUrl: './informe-list-modal.component.html',
  imports: [CommonModule, SharedModule, RouterModule],
})
export class InformeListModalComponent implements OnInit {
  @Input() pacienteId!: number;
  informes: IInforme[] = [];
  isLoading = false;

  constructor(
    public activeModal: NgbActiveModal,
    private informeService: InformeService,
  ) {}

  ngOnInit(): void {
    if (this.pacienteId) {
      this.isLoading = true;
      this.informeService.query({ 'pacienteId.in': this.pacienteId }).subscribe({
        next: res => {
          this.informes = res.body ?? [];
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
