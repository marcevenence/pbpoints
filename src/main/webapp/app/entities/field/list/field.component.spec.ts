import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FieldService } from '../service/field.service';

import { FieldComponent } from './field.component';

describe('Component Tests', () => {
  describe('Field Management Component', () => {
    let comp: FieldComponent;
    let fixture: ComponentFixture<FieldComponent>;
    let service: FieldService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FieldComponent],
      })
        .overrideTemplate(FieldComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FieldComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FieldService);

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
      expect(comp.fields?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
