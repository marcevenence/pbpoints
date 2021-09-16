import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ISuspension, Suspension } from '../suspension.model';
import { SuspensionService } from '../service/suspension.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-suspension-update',
  templateUrl: './suspension-update.component.html',
})
export class SuspensionUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [],
    endDate: [],
    user: [],
  });

  constructor(
    protected suspensionService: SuspensionService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ suspension }) => {
      this.updateForm(suspension);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const suspension = this.createFromForm();
    if (suspension.id !== undefined) {
      this.subscribeToSaveResponse(this.suspensionService.update(suspension));
    } else {
      this.subscribeToSaveResponse(this.suspensionService.create(suspension));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuspension>>): void {
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

  protected updateForm(suspension: ISuspension): void {
    this.editForm.patchValue({
      id: suspension.id,
      startDate: suspension.startDate ? suspension.startDate.format(DATE_FORMAT) : null,
      endDate: suspension.endDate ? suspension.endDate.format(DATE_FORMAT) : null,
      user: suspension.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, suspension.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ISuspension {
    return {
      ...new Suspension(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
