import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDireccion } from '../direccion.model';
import { DireccionService } from '../service/direccion.service';

@Component({
  templateUrl: './direccion-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DireccionDeleteDialogComponent {
  direccion?: IDireccion;

  protected direccionService = inject(DireccionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.direccionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
