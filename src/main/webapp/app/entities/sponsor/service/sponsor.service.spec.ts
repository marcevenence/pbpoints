import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISponsor, Sponsor } from '../sponsor.model';

import { SponsorService } from './sponsor.service';

describe('Service Tests', () => {
  describe('Sponsor Service', () => {
    let service: SponsorService;
    let httpMock: HttpTestingController;
    let elemDefault: ISponsor;
    let expectedResult: ISponsor | ISponsor[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SponsorService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        logoContentType: 'image/png',
        logo: 'AAAAAAA',
        name: 'AAAAAAA',
        active: false,
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

      it('should create a Sponsor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Sponsor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Sponsor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            logo: 'BBBBBB',
            name: 'BBBBBB',
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Sponsor', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Sponsor()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Sponsor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            logo: 'BBBBBB',
            name: 'BBBBBB',
            active: true,
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

      it('should delete a Sponsor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSponsorToCollectionIfMissing', () => {
        it('should add a Sponsor to an empty array', () => {
          const sponsor: ISponsor = { id: 123 };
          expectedResult = service.addSponsorToCollectionIfMissing([], sponsor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sponsor);
        });

        it('should not add a Sponsor to an array that contains it', () => {
          const sponsor: ISponsor = { id: 123 };
          const sponsorCollection: ISponsor[] = [
            {
              ...sponsor,
            },
            { id: 456 },
          ];
          expectedResult = service.addSponsorToCollectionIfMissing(sponsorCollection, sponsor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Sponsor to an array that doesn't contain it", () => {
          const sponsor: ISponsor = { id: 123 };
          const sponsorCollection: ISponsor[] = [{ id: 456 }];
          expectedResult = service.addSponsorToCollectionIfMissing(sponsorCollection, sponsor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sponsor);
        });

        it('should add only unique Sponsor to an array', () => {
          const sponsorArray: ISponsor[] = [{ id: 123 }, { id: 456 }, { id: 85047 }];
          const sponsorCollection: ISponsor[] = [{ id: 123 }];
          expectedResult = service.addSponsorToCollectionIfMissing(sponsorCollection, ...sponsorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sponsor: ISponsor = { id: 123 };
          const sponsor2: ISponsor = { id: 456 };
          expectedResult = service.addSponsorToCollectionIfMissing([], sponsor, sponsor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sponsor);
          expect(expectedResult).toContain(sponsor2);
        });

        it('should accept null and undefined values', () => {
          const sponsor: ISponsor = { id: 123 };
          expectedResult = service.addSponsorToCollectionIfMissing([], null, sponsor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sponsor);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
