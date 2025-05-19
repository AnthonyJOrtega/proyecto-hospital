import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEnfermedad } from '../enfermedad.model';

@Component({
  selector: 'jhi-enfermedad-detail',
  templateUrl: './enfermedad-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EnfermedadDetailComponent {
  enfermedad = input<IEnfermedad | null>(null);

  previousState(): void {
    window.history.back();
  }
}
