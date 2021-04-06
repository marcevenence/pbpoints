import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BracketService } from '../service/bracket.service';

import { BracketComponent } from './bracket.component';

describe('Component Tests', () => {
  describe('Bracket Management Component', () => {
    let comp: BracketComponent;
    let fixture: ComponentFixture<BracketComponent>;
    let service: BracketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BracketComponent],
      })
        .overrideTemplate(BracketComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BracketComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BracketService);

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
      expect(comp.brackets?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
