import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISuspension, Suspension } from '../suspension.model';

import { SuspensionService } from './suspension.service';

describe('Service Tests', () => {
  describe('Suspension Service', () => {
    let service: SuspensionService;
    let httpMock: HttpTestingController;
    let elemDefault: ISuspension;
    let expectedResult: ISuspension | ISuspension[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SuspensionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        startDate: currentDate,
        endDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Suspension', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Suspension()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Suspension', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Suspension', () => {
        const patchObject = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
          },
          new Suspension()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Suspension', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Suspension', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSuspensionToCollectionIfMissing', () => {
        it('should add a Suspension to an empty array', () => {
          const suspension: ISuspension = { id: 123 };
          expectedResult = service.addSuspensionToCollectionIfMissing([], suspension);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(suspension);
        });

        it('should not add a Suspension to an array that contains it', () => {
          const suspension: ISuspension = { id: 123 };
          const suspensionCollection: ISuspension[] = [
            {
              ...suspension,
            },
            { id: 456 },
          ];
          expectedResult = service.addSuspensionToCollectionIfMissing(suspensionCollection, suspension);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Suspension to an array that doesn't contain it", () => {
          const suspension: ISuspension = { id: 123 };
          const suspensionCollection: ISuspension[] = [{ id: 456 }];
          expectedResult = service.addSuspensionToCollectionIfMissing(suspensionCollection, suspension);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(suspension);
        });

        it('should add only unique Suspension to an array', () => {
          const suspensionArray: ISuspension[] = [{ id: 123 }, { id: 456 }, { id: 27699 }];
          const suspensionCollection: ISuspension[] = [{ id: 123 }];
          expectedResult = service.addSuspensionToCollectionIfMissing(suspensionCollection, ...suspensionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const suspension: ISuspension = { id: 123 };
          const suspension2: ISuspension = { id: 456 };
          expectedResult = service.addSuspensionToCollectionIfMissing([], suspension, suspension2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(suspension);
          expect(expectedResult).toContain(suspension2);
        });

        it('should accept null and undefined values', () => {
          const suspension: ISuspension = { id: 123 };
          expectedResult = service.addSuspensionToCollectionIfMissing([], null, suspension, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(suspension);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
