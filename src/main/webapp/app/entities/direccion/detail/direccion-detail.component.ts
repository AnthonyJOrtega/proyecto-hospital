import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDireccion } from '../direccion.model';

@Component({
  selector: 'jhi-direccion-detail',
  templateUrl: './direccion-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DireccionDetailComponent {
  direccion = input<IDireccion | null>(null);

  previousState(): void {
    window.history.back();
  }
}
