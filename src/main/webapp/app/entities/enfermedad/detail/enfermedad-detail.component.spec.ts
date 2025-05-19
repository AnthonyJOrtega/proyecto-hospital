import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EnfermedadDetailComponent } from './enfermedad-detail.component';

describe('Enfermedad Management Detail Component', () => {
  let comp: EnfermedadDetailComponent;
  let fixture: ComponentFixture<EnfermedadDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnfermedadDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./enfermedad-detail.component').then(m => m.EnfermedadDetailComponent),
              resolve: { enfermedad: () => of({ id: 11813 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EnfermedadDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnfermedadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load enfermedad on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EnfermedadDetailComponent);

      // THEN
      expect(instance.enfermedad()).toEqual(expect.objectContaining({ id: 11813 }));
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
