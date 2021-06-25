jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserExtraService } from '../service/user-extra.service';
import { IUserExtra, UserExtra } from '../user-extra.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDocType } from 'app/entities/doc-type/doc-type.model';
import { DocTypeService } from 'app/entities/doc-type/service/doc-type.service';

import { UserExtraUpdateComponent } from './user-extra-update.component';

describe('Component Tests', () => {
  describe('UserExtra Management Update Component', () => {
    let comp: UserExtraUpdateComponent;
    let fixture: ComponentFixture<UserExtraUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userExtraService: UserExtraService;
    let userService: UserService;
    let docTypeService: DocTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserExtraUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserExtraUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserExtraUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userExtraService = TestBed.inject(UserExtraService);
      userService = TestBed.inject(UserService);
      docTypeService = TestBed.inject(DocTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const userExtra: IUserExtra = { id: 456 };
        const user: IUser = { id: 27699 };
        userExtra.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ userExtra });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DocType query and add missing value', () => {
        const userExtra: IUserExtra = { id: 456 };
        const docType: IDocType = { id: 19378 };
        userExtra.docType = docType;

        const docTypeCollection: IDocType[] = [{ id: 91939 }];
        spyOn(docTypeService, 'query').and.returnValue(of(new HttpResponse({ body: docTypeCollection })));
        const additionalDocTypes = [docType];
        const expectedCollection: IDocType[] = [...additionalDocTypes, ...docTypeCollection];
        spyOn(docTypeService, 'addDocTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ userExtra });
        comp.ngOnInit();

        expect(docTypeService.query).toHaveBeenCalled();
        expect(docTypeService.addDocTypeToCollectionIfMissing).toHaveBeenCalledWith(docTypeCollection, ...additionalDocTypes);
        expect(comp.docTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userExtra: IUserExtra = { id: 456 };
        const user: IUser = { id: 47918 };
        userExtra.user = user;
        const docType: IDocType = { id: 99455 };
        userExtra.docType = docType;

        activatedRoute.data = of({ userExtra });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userExtra));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.docTypesSharedCollection).toContain(docType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const userExtra = { id: 123 };
        spyOn(userExtraService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ userExtra });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userExtra }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userExtraService.update).toHaveBeenCalledWith(userExtra);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const userExtra = new UserExtra();
        spyOn(userExtraService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ userExtra });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userExtra }));
        saveSubject.complete();

        // THEN
        expect(userExtraService.create).toHaveBeenCalledWith(userExtra);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const userExtra = { id: 123 };
        spyOn(userExtraService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ userExtra });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userExtraService.update).toHaveBeenCalledWith(userExtra);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDocTypeById', () => {
        it('Should return tracked DocType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
