jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocationService } from '../service/location.service';
import { ILocation, Location } from '../location.model';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

import { LocationUpdateComponent } from './location-update.component';

describe('Component Tests', () => {
  describe('Location Management Update Component', () => {
    let comp: LocationUpdateComponent;
    let fixture: ComponentFixture<LocationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let locationService: LocationService;
    let provinceService: ProvinceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      locationService = TestBed.inject(LocationService);
      provinceService = TestBed.inject(ProvinceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Province query and add missing value', () => {
        const location: ILocation = { id: 456 };
        const province: IProvince = { id: 60299 };
        location.province = province;

        const provinceCollection: IProvince[] = [{ id: 30077 }];
        spyOn(provinceService, 'query').and.returnValue(of(new HttpResponse({ body: provinceCollection })));
        const additionalProvinces = [province];
        const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
        spyOn(provinceService, 'addProvinceToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ location });
        comp.ngOnInit();

        expect(provinceService.query).toHaveBeenCalled();
        expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(provinceCollection, ...additionalProvinces);
        expect(comp.provincesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const location: ILocation = { id: 456 };
        const province: IProvince = { id: 85148 };
        location.province = province;

        activatedRoute.data = of({ location });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(location));
        expect(comp.provincesSharedCollection).toContain(province);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const location = { id: 123 };
        spyOn(locationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ location });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: location }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(locationService.update).toHaveBeenCalledWith(location);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const location = new Location();
        spyOn(locationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ location });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: location }));
        saveSubject.complete();

        // THEN
        expect(locationService.create).toHaveBeenCalledWith(location);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const location = { id: 123 };
        spyOn(locationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ location });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(locationService.update).toHaveBeenCalledWith(location);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProvinceById', () => {
        it('Should return tracked Province primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProvinceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
