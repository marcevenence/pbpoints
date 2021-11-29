import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EquipmentService } from '../service/equipment.service';

import { EquipmentComponent } from './equipment.component';

describe('Component Tests', () => {
  describe('Equipment Management Component', () => {
    let comp: EquipmentComponent;
    let fixture: ComponentFixture<EquipmentComponent>;
    let service: EquipmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EquipmentComponent],
      })
        .overrideTemplate(EquipmentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EquipmentComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EquipmentService);

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
      expect(comp.equipment?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
