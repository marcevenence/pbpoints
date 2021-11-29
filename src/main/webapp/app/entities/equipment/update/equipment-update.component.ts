import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEquipment, Equipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-equipment-update',
  templateUrl: './equipment-update.component.html',
})
export class EquipmentUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    brand: [null, [Validators.required]],
    model: [null, [Validators.required]],
    picture1: [null, [Validators.required]],
    picture1ContentType: [],
    picture2: [null, [Validators.required]],
    picture2ContentType: [],
    serial: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected equipmentService: EquipmentService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipment }) => {
      this.updateForm(equipment);

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
    const equipment = this.createFromForm();
    if (equipment.id !== undefined) {
      this.subscribeToSaveResponse(this.equipmentService.update(equipment));
    } else {
      this.subscribeToSaveResponse(this.equipmentService.create(equipment));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipment>>): void {
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

  protected updateForm(equipment: IEquipment): void {
    this.editForm.patchValue({
      id: equipment.id,
      brand: equipment.brand,
      model: equipment.model,
      picture1: equipment.picture1,
      picture1ContentType: equipment.picture1ContentType,
      picture2: equipment.picture2,
      picture2ContentType: equipment.picture2ContentType,
      serial: equipment.serial,
      user: equipment.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, equipment.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IEquipment {
    return {
      ...new Equipment(),
      id: this.editForm.get(['id'])!.value,
      brand: this.editForm.get(['brand'])!.value,
      model: this.editForm.get(['model'])!.value,
      picture1ContentType: this.editForm.get(['picture1ContentType'])!.value,
      picture1: this.editForm.get(['picture1'])!.value,
      picture2ContentType: this.editForm.get(['picture2ContentType'])!.value,
      picture2: this.editForm.get(['picture2'])!.value,
      serial: this.editForm.get(['serial'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
