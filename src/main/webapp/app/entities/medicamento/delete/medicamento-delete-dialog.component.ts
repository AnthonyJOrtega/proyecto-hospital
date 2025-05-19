import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicamento } from '../medicamento.model';
import { MedicamentoService } from '../service/medicamento.service';

@Component({
  templateUrl: './medicamento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicamentoDeleteDialogComponent {
  medicamento?: IMedicamento;

  protected medicamentoService = inject(MedicamentoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
