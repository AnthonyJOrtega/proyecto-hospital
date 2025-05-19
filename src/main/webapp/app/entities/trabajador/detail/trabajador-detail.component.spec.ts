import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrabajadorDetailComponent } from './trabajador-detail.component';

describe('Trabajador Management Detail Component', () => {
  let comp: TrabajadorDetailComponent;
  let fixture: ComponentFixture<TrabajadorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrabajadorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./trabajador-detail.component').then(m => m.TrabajadorDetailComponent),
              resolve: { trabajador: () => of({ id: 528 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrabajadorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrabajadorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load trabajador on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrabajadorDetailComponent);

      // THEN
      expect(instance.trabajador()).toEqual(expect.objectContaining({ id: 528 }));
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
