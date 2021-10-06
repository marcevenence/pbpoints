import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MainRosterDetailComponent } from './main-roster-detail.component';

describe('Component Tests', () => {
  describe('MainRoster Management Detail Component', () => {
    let comp: MainRosterDetailComponent;
    let fixture: ComponentFixture<MainRosterDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MainRosterDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mainRoster: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MainRosterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MainRosterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mainRoster on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mainRoster).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
