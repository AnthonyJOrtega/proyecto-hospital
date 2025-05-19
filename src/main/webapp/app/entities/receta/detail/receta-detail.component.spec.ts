import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RecetaDetailComponent } from './receta-detail.component';

describe('Receta Management Detail Component', () => {
  let comp: RecetaDetailComponent;
  let fixture: ComponentFixture<RecetaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecetaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./receta-detail.component').then(m => m.RecetaDetailComponent),
              resolve: { receta: () => of({ id: 15537 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RecetaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecetaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load receta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RecetaDetailComponent);

      // THEN
      expect(instance.receta()).toEqual(expect.objectContaining({ id: 15537 }));
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
