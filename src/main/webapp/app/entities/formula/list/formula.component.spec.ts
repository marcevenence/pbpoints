import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FormulaService } from '../service/formula.service';

import { FormulaComponent } from './formula.component';

describe('Component Tests', () => {
  describe('Formula Management Component', () => {
    let comp: FormulaComponent;
    let fixture: ComponentFixture<FormulaComponent>;
    let service: FormulaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FormulaComponent],
      })
        .overrideTemplate(FormulaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormulaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FormulaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.formulas?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
