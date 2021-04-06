import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BracketDetailComponent } from './bracket-detail.component';

describe('Component Tests', () => {
  describe('Bracket Management Detail Component', () => {
    let comp: BracketDetailComponent;
    let fixture: ComponentFixture<BracketDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BracketDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ bracket: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BracketDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BracketDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bracket on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bracket).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
