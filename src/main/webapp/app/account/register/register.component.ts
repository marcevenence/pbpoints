import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IDocType } from 'app/entities/doc-type/doc-type.model';
import { DocTypeService } from 'app/entities/doc-type/service/doc-type.service';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import { RegisterService } from './register.service';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;
  docTypes: IDocType[] = [];

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  registerForm = this.fb.group({
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(15)]],
    numDoc: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    bornDate: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(10)]],
    picture: ['', [Validators.required]],
    pictureContentType: [],
    code: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],
    docType: [],
  });

  constructor(
    protected eventManager: EventManager,
    protected dataUtils: DataUtils,
    protected elementRef: ElementRef,
    private translateService: TranslateService,
    private registerService: RegisterService,
    private docTypeService: DocTypeService,
    private fb: FormBuilder
  ) {}

  ngAfterViewInit(): void {
    this.docTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocType[]>) => res.body ?? []))
      .pipe(
        map((docTypes: IDocType[]) =>
          this.docTypeService.addDocTypeToCollectionIfMissing(docTypes, this.registerForm.get('docType')!.value)
        )
      )
      .subscribe((docTypes: IDocType[]) => (this.docTypes = docTypes));

    if (this.login) {
      this.login.nativeElement.focus();
    }
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const password = this.registerForm.get(['password'])!.value;
    const phone = this.registerForm.get(['phone'])!.value;
    const numDoc = this.registerForm.get(['numDoc'])!.value;
    const bornDate = this.registerForm.get(['bornDate'])!.value;
    const picture = this.registerForm.get(['picture'])!.value;
    const pictureContentType = this.registerForm.get(['pictureContentType'])!.value;
    const code = this.registerForm.get(['code'])!.value;
    const docType = this.registerForm.get(['docType'])!.value;
    if (password !== this.registerForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      const login = this.registerForm.get(['login'])!.value;
      const email = this.registerForm.get(['email'])!.value;
      this.registerService
        .save({
          login,
          email,
          password,
          langKey: this.translateService.currentLang,
          phone,
          numDoc,
          bornDate,
          picture,
          pictureContentType,
          code,
          docType,
        })
        .subscribe(
          () => (this.success = true),
          response => this.processError(response)
        );
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  trackDocTypeById(index: number, item: IDocType): number {
    return item.id!;
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.registerForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('pbpointsApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.registerForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }
}
