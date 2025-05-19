import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrabajador } from '../trabajador.model';
import { TrabajadorService } from '../service/trabajador.service';

@Component({
  templateUrl: './trabajador-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrabajadorDeleteDialogComponent {
  trabajador?: ITrabajador;

  protected trabajadorService = inject(TrabajadorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trabajadorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
