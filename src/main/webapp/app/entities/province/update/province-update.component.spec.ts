jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProvinceService } from '../service/province.service';
import { IProvince, Province } from '../province.model';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

import { ProvinceUpdateComponent } from './province-update.component';

describe('Component Tests', () => {
  describe('Province Management Update Component', () => {
    let comp: ProvinceUpdateComponent;
    let fixture: ComponentFixture<ProvinceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let provinceService: ProvinceService;
    let countryService: CountryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProvinceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProvinceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProvinceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      provinceService = TestBed.inject(ProvinceService);
      countryService = TestBed.inject(CountryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Country query and add missing value', () => {
        const province: IProvince = { id: 456 };
        const country: ICountry = { id: 45185 };
        province.country = country;

        const countryCollection: ICountry[] = [{ id: 82050 }];
        spyOn(countryService, 'query').and.returnValue(of(new HttpResponse({ body: countryCollection })));
        const additionalCountries = [country];
        const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
        spyOn(countryService, 'addCountryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ province });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryCollection, ...additionalCountries);
        expect(comp.countriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const province: IProvince = { id: 456 };
        const country: ICountry = { id: 5164 };
        province.country = country;

        activatedRoute.data = of({ province });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(province));
        expect(comp.countriesSharedCollection).toContain(country);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const province = { id: 123 };
        spyOn(provinceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ province });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: province }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(provinceService.update).toHaveBeenCalledWith(province);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const province = new Province();
        spyOn(provinceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ province });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: province }));
        saveSubject.complete();

        // THEN
        expect(provinceService.create).toHaveBeenCalledWith(province);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const province = { id: 123 };
        spyOn(provinceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ province });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(provinceService.update).toHaveBeenCalledWith(province);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCountryById', () => {
        it('Should return tracked Country primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCountryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
