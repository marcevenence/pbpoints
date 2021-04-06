import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocTypeDetailComponent } from './doc-type-detail.component';

describe('Component Tests', () => {
  describe('DocType Management Detail Component', () => {
    let comp: DocTypeDetailComponent;
    let fixture: ComponentFixture<DocTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ docType: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load docType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.docType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
