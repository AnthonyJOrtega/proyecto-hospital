import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReceta } from '../receta.model';
import { RecetaService } from '../service/receta.service';

@Component({
  templateUrl: './receta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RecetaDeleteDialogComponent {
  receta?: IReceta;

  protected recetaService = inject(RecetaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recetaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
