jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BracketService } from '../service/bracket.service';
import { IBracket, Bracket } from '../bracket.model';

import { BracketUpdateComponent } from './bracket-update.component';

describe('Component Tests', () => {
  describe('Bracket Management Update Component', () => {
    let comp: BracketUpdateComponent;
    let fixture: ComponentFixture<BracketUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let bracketService: BracketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BracketUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BracketUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BracketUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      bracketService = TestBed.inject(BracketService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const bracket: IBracket = { id: 456 };

        activatedRoute.data = of({ bracket });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bracket));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bracket = { id: 123 };
        spyOn(bracketService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bracket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bracket }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(bracketService.update).toHaveBeenCalledWith(bracket);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bracket = new Bracket();
        spyOn(bracketService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bracket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bracket }));
        saveSubject.complete();

        // THEN
        expect(bracketService.create).toHaveBeenCalledWith(bracket);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bracket = { id: 123 };
        spyOn(bracketService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bracket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(bracketService.update).toHaveBeenCalledWith(bracket);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
