import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInforme } from '../informe.model';
import { InformeService } from '../service/informe.service';

@Component({
  templateUrl: './informe-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InformeDeleteDialogComponent {
  informe?: IInforme;

  protected informeService = inject(InformeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.informeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
