jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EventCategoryService } from '../service/event-category.service';
import { IEventCategory, EventCategory } from '../event-category.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IFormat } from 'app/entities/format/format.model';
import { FormatService } from 'app/entities/format/service/format.service';

import { EventCategoryUpdateComponent } from './event-category-update.component';

describe('Component Tests', () => {
  describe('EventCategory Management Update Component', () => {
    let comp: EventCategoryUpdateComponent;
    let fixture: ComponentFixture<EventCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let eventCategoryService: EventCategoryService;
    let eventService: EventService;
    let categoryService: CategoryService;
    let formatService: FormatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EventCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      eventCategoryService = TestBed.inject(EventCategoryService);
      eventService = TestBed.inject(EventService);
      categoryService = TestBed.inject(CategoryService);
      formatService = TestBed.inject(FormatService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Event query and add missing value', () => {
        const eventCategory: IEventCategory = { id: 456 };
        const event: IEvent = { id: 49927 };
        eventCategory.event = event;

        const eventCollection: IEvent[] = [{ id: 63979 }];
        spyOn(eventService, 'query').and.returnValue(of(new HttpResponse({ body: eventCollection })));
        const additionalEvents = [event];
        const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
        spyOn(eventService, 'addEventToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ eventCategory });
        comp.ngOnInit();

        expect(eventService.query).toHaveBeenCalled();
        expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(eventCollection, ...additionalEvents);
        expect(comp.eventsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Category query and add missing value', () => {
        const eventCategory: IEventCategory = { id: 456 };
        const category: ICategory = { id: 22022 };
        eventCategory.category = category;

        const categoryCollection: ICategory[] = [{ id: 75530 }];
        spyOn(categoryService, 'query').and.returnValue(of(new HttpResponse({ body: categoryCollection })));
        const additionalCategories = [category];
        const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
        spyOn(categoryService, 'addCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ eventCategory });
        comp.ngOnInit();

        expect(categoryService.query).toHaveBeenCalled();
        expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Format query and add missing value', () => {
        const eventCategory: IEventCategory = { id: 456 };
        const format: IFormat = { id: 96323 };
        eventCategory.format = format;

        const formatCollection: IFormat[] = [{ id: 55773 }];
        spyOn(formatService, 'query').and.returnValue(of(new HttpResponse({ body: formatCollection })));
        const additionalFormats = [format];
        const expectedCollection: IFormat[] = [...additionalFormats, ...formatCollection];
        spyOn(formatService, 'addFormatToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ eventCategory });
        comp.ngOnInit();

        expect(formatService.query).toHaveBeenCalled();
        expect(formatService.addFormatToCollectionIfMissing).toHaveBeenCalledWith(formatCollection, ...additionalFormats);
        expect(comp.formatsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const eventCategory: IEventCategory = { id: 456 };
        const event: IEvent = { id: 13456 };
        eventCategory.event = event;
        const category: ICategory = { id: 82897 };
        eventCategory.category = category;
        const format: IFormat = { id: 96350 };
        eventCategory.format = format;

        activatedRoute.data = of({ eventCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(eventCategory));
        expect(comp.eventsSharedCollection).toContain(event);
        expect(comp.categoriesSharedCollection).toContain(category);
        expect(comp.formatsSharedCollection).toContain(format);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const eventCategory = { id: 123 };
        spyOn(eventCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ eventCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: eventCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(eventCategoryService.update).toHaveBeenCalledWith(eventCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const eventCategory = new EventCategory();
        spyOn(eventCategoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ eventCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: eventCategory }));
        saveSubject.complete();

        // THEN
        expect(eventCategoryService.create).toHaveBeenCalledWith(eventCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const eventCategory = { id: 123 };
        spyOn(eventCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ eventCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(eventCategoryService.update).toHaveBeenCalledWith(eventCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEventById', () => {
        it('Should return tracked Event primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEventById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCategoryById', () => {
        it('Should return tracked Category primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFormatById', () => {
        it('Should return tracked Format primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFormatById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
