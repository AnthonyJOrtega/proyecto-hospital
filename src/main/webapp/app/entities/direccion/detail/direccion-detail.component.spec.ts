import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DireccionDetailComponent } from './direccion-detail.component';

describe('Direccion Management Detail Component', () => {
  let comp: DireccionDetailComponent;
  let fixture: ComponentFixture<DireccionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DireccionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./direccion-detail.component').then(m => m.DireccionDetailComponent),
              resolve: { direccion: () => of({ id: 31929 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DireccionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DireccionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load direccion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DireccionDetailComponent);

      // THEN
      expect(instance.direccion()).toEqual(expect.objectContaining({ id: 31929 }));
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
