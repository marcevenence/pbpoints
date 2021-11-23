jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FieldService } from '../service/field.service';
import { IField, Field } from '../field.model';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';

import { FieldUpdateComponent } from './field-update.component';

describe('Component Tests', () => {
  describe('Field Management Update Component', () => {
    let comp: FieldUpdateComponent;
    let fixture: ComponentFixture<FieldUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fieldService: FieldService;
    let cityService: CityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FieldUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FieldUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FieldUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fieldService = TestBed.inject(FieldService);
      cityService = TestBed.inject(CityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call City query and add missing value', () => {
        const field: IField = { id: 456 };
        const city: ICity = { id: 3708 };
        field.city = city;

        const cityCollection: ICity[] = [{ id: 4451 }];
        spyOn(cityService, 'query').and.returnValue(of(new HttpResponse({ body: cityCollection })));
        const additionalCities = [city];
        const expectedCollection: ICity[] = [...additionalCities, ...cityCollection];
        spyOn(cityService, 'addCityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ field });
        comp.ngOnInit();

        expect(cityService.query).toHaveBeenCalled();
        expect(cityService.addCityToCollectionIfMissing).toHaveBeenCalledWith(cityCollection, ...additionalCities);
        expect(comp.citiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const field: IField = { id: 456 };
        const city: ICity = { id: 97304 };
        field.city = city;

        activatedRoute.data = of({ field });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(field));
        expect(comp.citiesSharedCollection).toContain(city);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const field = { id: 123 };
        spyOn(fieldService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ field });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: field }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fieldService.update).toHaveBeenCalledWith(field);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const field = new Field();
        spyOn(fieldService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ field });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: field }));
        saveSubject.complete();

        // THEN
        expect(fieldService.create).toHaveBeenCalledWith(field);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const field = { id: 123 };
        spyOn(fieldService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ field });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fieldService.update).toHaveBeenCalledWith(field);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCityById', () => {
        it('Should return tracked City primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
