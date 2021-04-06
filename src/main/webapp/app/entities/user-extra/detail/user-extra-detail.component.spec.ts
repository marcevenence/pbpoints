import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { UserExtraDetailComponent } from './user-extra-detail.component';

describe('Component Tests', () => {
  describe('UserExtra Management Detail Component', () => {
    let comp: UserExtraDetailComponent;
    let fixture: ComponentFixture<UserExtraDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UserExtraDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ userExtra: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UserExtraDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserExtraDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load userExtra on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userExtra).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
