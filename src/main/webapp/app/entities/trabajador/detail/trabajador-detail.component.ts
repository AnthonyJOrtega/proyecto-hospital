import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITrabajador } from '../trabajador.model';

@Component({
  selector: 'jhi-trabajador-detail',
  templateUrl: './trabajador-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TrabajadorDetailComponent {
  trabajador = input<ITrabajador | null>(null);

  previousState(): void {
    window.history.back();
  }
}
