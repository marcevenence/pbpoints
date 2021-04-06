import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEventCategory, EventCategory } from '../event-category.model';

import { EventCategoryService } from './event-category.service';

describe('Service Tests', () => {
  describe('EventCategory Service', () => {
    let service: EventCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IEventCategory;
    let expectedResult: IEventCategory | IEventCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EventCategoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        splitDeck: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a EventCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EventCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EventCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            splitDeck: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EventCategory', () => {
        const patchObject = Object.assign(
          {
            splitDeck: true,
          },
          new EventCategory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EventCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            splitDeck: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EventCategory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEventCategoryToCollectionIfMissing', () => {
        it('should add a EventCategory to an empty array', () => {
          const eventCategory: IEventCategory = { id: 123 };
          expectedResult = service.addEventCategoryToCollectionIfMissing([], eventCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(eventCategory);
        });

        it('should not add a EventCategory to an array that contains it', () => {
          const eventCategory: IEventCategory = { id: 123 };
          const eventCategoryCollection: IEventCategory[] = [
            {
              ...eventCategory,
            },
            { id: 456 },
          ];
          expectedResult = service.addEventCategoryToCollectionIfMissing(eventCategoryCollection, eventCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EventCategory to an array that doesn't contain it", () => {
          const eventCategory: IEventCategory = { id: 123 };
          const eventCategoryCollection: IEventCategory[] = [{ id: 456 }];
          expectedResult = service.addEventCategoryToCollectionIfMissing(eventCategoryCollection, eventCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(eventCategory);
        });

        it('should add only unique EventCategory to an array', () => {
          const eventCategoryArray: IEventCategory[] = [{ id: 123 }, { id: 456 }, { id: 32956 }];
          const eventCategoryCollection: IEventCategory[] = [{ id: 123 }];
          expectedResult = service.addEventCategoryToCollectionIfMissing(eventCategoryCollection, ...eventCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const eventCategory: IEventCategory = { id: 123 };
          const eventCategory2: IEventCategory = { id: 456 };
          expectedResult = service.addEventCategoryToCollectionIfMissing([], eventCategory, eventCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(eventCategory);
          expect(expectedResult).toContain(eventCategory2);
        });

        it('should accept null and undefined values', () => {
          const eventCategory: IEventCategory = { id: 123 };
          expectedResult = service.addEventCategoryToCollectionIfMissing([], null, eventCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(eventCategory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
