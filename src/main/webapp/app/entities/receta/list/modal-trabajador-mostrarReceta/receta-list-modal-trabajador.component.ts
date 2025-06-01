import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IReceta } from '../../receta.model';
import { RecetaService } from '../../service/receta.service';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-receta-list-modal-trabajador',
  templateUrl: './receta-list-modal-trabajador.component.html',
  imports: [CommonModule, SharedModule, RouterModule],
})
export class RecetaListModalTrabajadorComponent implements OnInit {
  @Input() trabajadorId!: number;
  recetas: IReceta[] = [];
  isLoading = false;

  constructor(
    public activeModal: NgbActiveModal,
    private recetaService: RecetaService,
  ) {}

  ngOnInit(): void {
    if (this.trabajadorId) {
      this.isLoading = true;
      this.recetaService.query({ 'trabajadorId.in': this.trabajadorId }).subscribe({
        next: res => {
          this.recetas = res.body ?? [];
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
