import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserExtra, UserExtra } from '../user-extra.model';
import { UserExtraService } from '../service/user-extra.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDocType } from 'app/entities/doc-type/doc-type.model';
import { DocTypeService } from 'app/entities/doc-type/service/doc-type.service';

@Component({
  selector: 'jhi-user-extra-update',
  templateUrl: './user-extra-update.component.html',
})
export class UserExtraUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  docTypesSharedCollection: IDocType[] = [];

  editForm = this.fb.group({
    id: [],
    numDoc: [],
    phone: [],
    bornDate: [],
    picture: [null, [Validators.required]],
    pictureContentType: [],
    code: [null, [Validators.required]],
    user: [null, Validators.required],
    docType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected userExtraService: UserExtraService,
    protected userService: UserService,
    protected docTypeService: DocTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userExtra }) => {
      this.updateForm(userExtra);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('pbpointsApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userExtra = this.createFromForm();
    if (userExtra.id !== undefined) {
      this.subscribeToSaveResponse(this.userExtraService.update(userExtra));
    } else {
      this.subscribeToSaveResponse(this.userExtraService.create(userExtra));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackDocTypeById(index: number, item: IDocType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserExtra>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userExtra: IUserExtra): void {
    this.editForm.patchValue({
      id: userExtra.id,
      numDoc: userExtra.numDoc,
      phone: userExtra.phone,
      bornDate: userExtra.bornDate,
      picture: userExtra.picture,
      pictureContentType: userExtra.pictureContentType,
      code: userExtra.code,
      user: userExtra.user,
      docType: userExtra.docType,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userExtra.user);
    this.docTypesSharedCollection = this.docTypeService.addDocTypeToCollectionIfMissing(this.docTypesSharedCollection, userExtra.docType);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.docTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocType[]>) => res.body ?? []))
      .pipe(
        map((docTypes: IDocType[]) => this.docTypeService.addDocTypeToCollectionIfMissing(docTypes, this.editForm.get('docType')!.value))
      )
      .subscribe((docTypes: IDocType[]) => (this.docTypesSharedCollection = docTypes));
  }

  protected createFromForm(): IUserExtra {
    return {
      ...new UserExtra(),
      id: this.editForm.get(['id'])!.value,
      numDoc: this.editForm.get(['numDoc'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      bornDate: this.editForm.get(['bornDate'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
      code: this.editForm.get(['code'])!.value,
      user: this.editForm.get(['user'])!.value,
      docType: this.editForm.get(['docType'])!.value,
    };
  }
}
