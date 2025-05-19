import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InformeDetailComponent } from './informe-detail.component';

describe('Informe Management Detail Component', () => {
  let comp: InformeDetailComponent;
  let fixture: ComponentFixture<InformeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InformeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./informe-detail.component').then(m => m.InformeDetailComponent),
              resolve: { informe: () => of({ id: 15736 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InformeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InformeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load informe on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InformeDetailComponent);

      // THEN
      expect(instance.informe()).toEqual(expect.objectContaining({ id: 15736 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
