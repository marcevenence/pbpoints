import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDocType, DocType } from '../doc-type.model';
import { DocTypeService } from '../service/doc-type.service';

@Component({
  selector: 'jhi-doc-type-update',
  templateUrl: './doc-type-update.component.html',
})
export class DocTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(protected docTypeService: DocTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ docType }) => {
      this.updateForm(docType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const docType = this.createFromForm();
    if (docType.id !== undefined) {
      this.subscribeToSaveResponse(this.docTypeService.update(docType));
    } else {
      this.subscribeToSaveResponse(this.docTypeService.create(docType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocType>>): void {
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

  protected updateForm(docType: IDocType): void {
    this.editForm.patchValue({
      id: docType.id,
      name: docType.name,
      description: docType.description,
    });
  }

  protected createFromForm(): IDocType {
    return {
      ...new DocType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
