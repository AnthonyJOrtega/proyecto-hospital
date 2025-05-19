import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IReceta } from '../receta.model';

@Component({
  selector: 'jhi-receta-detail',
  templateUrl: './receta-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class RecetaDetailComponent {
  receta = input<IReceta | null>(null);

  previousState(): void {
    window.history.back();
  }
}
