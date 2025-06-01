import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IInforme } from '../../informe.model';
import { InformeService } from '../../service/informe.service';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-informe-list-modal-trabajador',
  templateUrl: './informe-list-modal-trabajador.component.html',
  imports: [CommonModule, SharedModule, RouterModule],
})
export class InformeListModalTrabajadorComponent implements OnInit {
  @Input() trabajadorId!: number;
  informes: IInforme[] = [];
  isLoading = false;

  constructor(
    public activeModal: NgbActiveModal,
    private informeService: InformeService,
  ) {}

  ngOnInit(): void {
    if (this.trabajadorId) {
      this.isLoading = true;
      this.informeService.query({ 'trabajadorId.in': this.trabajadorId }).subscribe({
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
