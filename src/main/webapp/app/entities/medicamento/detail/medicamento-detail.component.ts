import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMedicamento } from '../medicamento.model';

@Component({
  selector: 'jhi-medicamento-detail',
  templateUrl: './medicamento-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MedicamentoDetailComponent {
  medicamento = input<IMedicamento | null>(null);

  previousState(): void {
    window.history.back();
  }
}
