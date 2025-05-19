import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IInforme } from '../informe.model';

@Component({
  selector: 'jhi-informe-detail',
  templateUrl: './informe-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class InformeDetailComponent {
  informe = input<IInforme | null>(null);

  previousState(): void {
    window.history.back();
  }
}
