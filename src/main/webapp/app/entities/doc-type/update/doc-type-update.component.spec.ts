jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocTypeService } from '../service/doc-type.service';
import { IDocType, DocType } from '../doc-type.model';

import { DocTypeUpdateComponent } from './doc-type-update.component';

describe('Component Tests', () => {
  describe('DocType Management Update Component', () => {
    let comp: DocTypeUpdateComponent;
    let fixture: ComponentFixture<DocTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let docTypeService: DocTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      docTypeService = TestBed.inject(DocTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const docType: IDocType = { id: 456 };

        activatedRoute.data = of({ docType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(docType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const docType = { id: 123 };
        spyOn(docTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ docType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: docType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(docTypeService.update).toHaveBeenCalledWith(docType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const docType = new DocType();
        spyOn(docTypeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ docType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: docType }));
        saveSubject.complete();

        // THEN
        expect(docTypeService.create).toHaveBeenCalledWith(docType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const docType = { id: 123 };
        spyOn(docTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ docType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(docTypeService.update).toHaveBeenCalledWith(docType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
