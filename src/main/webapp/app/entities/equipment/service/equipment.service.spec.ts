import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEquipment, Equipment } from '../equipment.model';

import { EquipmentService } from './equipment.service';

describe('Service Tests', () => {
  describe('Equipment Service', () => {
    let service: EquipmentService;
    let httpMock: HttpTestingController;
    let elemDefault: IEquipment;
    let expectedResult: IEquipment | IEquipment[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EquipmentService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        brand: 'AAAAAAA',
        model: 'AAAAAAA',
        picture1ContentType: 'image/png',
        picture1: 'AAAAAAA',
        picture2ContentType: 'image/png',
        picture2: 'AAAAAAA',
        serial: 'AAAAAAA',
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

      it('should create a Equipment', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Equipment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Equipment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            brand: 'BBBBBB',
            model: 'BBBBBB',
            picture1: 'BBBBBB',
            picture2: 'BBBBBB',
            serial: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Equipment', () => {
        const patchObject = Object.assign(
          {
            brand: 'BBBBBB',
            model: 'BBBBBB',
            picture1: 'BBBBBB',
            serial: 'BBBBBB',
          },
          new Equipment()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Equipment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            brand: 'BBBBBB',
            model: 'BBBBBB',
            picture1: 'BBBBBB',
            picture2: 'BBBBBB',
            serial: 'BBBBBB',
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

      it('should delete a Equipment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEquipmentToCollectionIfMissing', () => {
        it('should add a Equipment to an empty array', () => {
          const equipment: IEquipment = { id: 123 };
          expectedResult = service.addEquipmentToCollectionIfMissing([], equipment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(equipment);
        });

        it('should not add a Equipment to an array that contains it', () => {
          const equipment: IEquipment = { id: 123 };
          const equipmentCollection: IEquipment[] = [
            {
              ...equipment,
            },
            { id: 456 },
          ];
          expectedResult = service.addEquipmentToCollectionIfMissing(equipmentCollection, equipment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Equipment to an array that doesn't contain it", () => {
          const equipment: IEquipment = { id: 123 };
          const equipmentCollection: IEquipment[] = [{ id: 456 }];
          expectedResult = service.addEquipmentToCollectionIfMissing(equipmentCollection, equipment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(equipment);
        });

        it('should add only unique Equipment to an array', () => {
          const equipmentArray: IEquipment[] = [{ id: 123 }, { id: 456 }, { id: 73537 }];
          const equipmentCollection: IEquipment[] = [{ id: 123 }];
          expectedResult = service.addEquipmentToCollectionIfMissing(equipmentCollection, ...equipmentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const equipment: IEquipment = { id: 123 };
          const equipment2: IEquipment = { id: 456 };
          expectedResult = service.addEquipmentToCollectionIfMissing([], equipment, equipment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(equipment);
          expect(expectedResult).toContain(equipment2);
        });

        it('should accept null and undefined values', () => {
          const equipment: IEquipment = { id: 123 };
          expectedResult = service.addEquipmentToCollectionIfMissing([], null, equipment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(equipment);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
