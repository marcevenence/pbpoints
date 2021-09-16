import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { LANGUAGES } from 'app/config/language.constants';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  account!: Account;
  success = false;
  languages = LANGUAGES;
  settingsForm = this.fb.group({
    id: [],
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]],
    numDoc: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    bornDate: [],
    code: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],

    langKey: [undefined],
    login: [],
    imageUrl: [],
  });

  constructor(private accountService: AccountService, private fb: FormBuilder, private translateService: TranslateService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.settingsForm.patchValue({
          id: account.id,
          firstName: account.firstName,
          lastName: account.lastName,
          email: account.email,
          langKey: account.langKey,
          login: account.login,
          imageUrl: account.imageUrl,
          phone: account.phone,
          numDoc: account.numDoc,
          bornDate: account.bornDate,
          code: account.code,
        });

        this.account = account;
      }
    });
  }

  save(): void {
    this.success = false;
    this.account.id = this.settingsForm.get('id')!.value;
    this.account.firstName = this.settingsForm.get('firstName')!.value;
    this.account.lastName = this.settingsForm.get('lastName')!.value;
    this.account.email = this.settingsForm.get('email')!.value;
    this.account.langKey = this.settingsForm.get('langKey')!.value;
    this.account.login = this.settingsForm.get('login')!.value;
    this.account.imageUrl = this.settingsForm.get('imageUrl')!.value;
    this.account.phone = this.settingsForm.get(['phone'])!.value;
    this.account.numDoc = this.settingsForm.get(['numDoc'])!.value;
    this.account.code = this.settingsForm.get(['code'])!.value;
    this.account.bornDate = this.settingsForm.get(['bornDate'])!.value;

    this.accountService.save(this.account).subscribe(() => {
      this.success = true;

      this.accountService.authenticate(this.account);

      if (this.account.langKey !== this.translateService.currentLang) {
        this.translateService.use(this.account.langKey);
      }
    });
  }
}
