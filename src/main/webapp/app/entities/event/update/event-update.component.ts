import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT, DATE_FORMAT } from 'app/config/input.constants';

import { IEvent, Event } from '../event.model';
import { EventService } from '../service/event.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';
import { IField } from 'app/entities/field/field.model';
import { FieldService } from 'app/entities/field/service/field.service';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);

  tournamentsSharedCollection: ITournament[] = [];
  fieldsSharedCollection: IField[] = [];
  seasonsSharedCollection: ISeason[] = [];

  editForm = this.fb.group(
    {
      id: [],
      name: [],
      fromDate: ['', Validators.required],
      endDate: ['', Validators.required],
      startInscriptionDate: ['', Validators.required],
      endInscriptionDate: ['', Validators.required],
      endInscriptionPlayersDate: [null, [Validators.required]],
      status: [],
      createDate: [],
      updatedDate: [],
      field: [null, Validators.required],
      tournament: [],
      season: [null, Validators.required],
    },
    { validator: this.dateLessThan('fromDate', 'endDate', 'endInscriptionPlayersDate', 'endInscriptionDate') }
  );

  constructor(
    protected eventService: EventService,
    protected tournamentService: TournamentService,
    protected fieldService: FieldService,
    protected seasonService: SeasonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  dateLessThan(from: string, to: string, endP: string, end: string): any {
    return (group: FormGroup): { [key: string]: any } => {
      const f = group.controls[from];
      const t = group.controls[to];
      const eP = group.controls[endP];
      const e = group.controls[end];
      if (f.value > t.value) {
        return {
          dates: 'Date from should be less than Date to',
        };
      }
      if (f.value < e.value) {
        return {
          dates: 'Date from should be higher than Inscription Date',
        };
      }
      if (f.value < eP.value) {
        return {
          dates: 'Date from should be Higher than Inscription Player Date',
        };
      }
      if (eP.value > t.value) {
        return {
          dates: 'Inscription Player Date should be less than Date To',
        };
      }
      if (e.value < eP.value) {
        return {
          dates: 'Inscription Date should be higher than Inscription player Date',
        };
      }
      if (e.value > t.value) {
        return {
          dates: 'Inscription Date should be less than Date To',
        };
      }
      return {};
    };
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      if (event.id === undefined) {
        const today = dayjs().startOf('day');
        event.createDate = today;
        event.updatedDate = today;
      }
      this.updateForm(event);
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  trackFieldById(index: number, item: IField): number {
    return item.id!;
  }

  trackSeasonById(index: number, item: ISeason): number {
    return item.id!;
  }

  onEndDateChange(): void {
    if (this.editForm.get('tournament')!.value?.closeInscrDays !== null && this.editForm.get(['endDate'])!.value != null) {
      this.editForm.patchValue({ endInscriptionDate: this.editForm.get(['endDate'])!.value });
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
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

  protected updateForm(event: IEvent): void {
    this.editForm.patchValue({
      id: event.id,
      name: event.name,
      fromDate: event.fromDate ? event.fromDate.format(DATE_FORMAT) : null,
      endDate: event.endDate ? event.endDate.format(DATE_FORMAT) : null,
      endInscriptionPlayersDate: event.endInscriptionPlayersDate ? event.endInscriptionPlayersDate.format(DATE_FORMAT) : null,
      startInscriptionDate: event.startInscriptionDate,
      endInscriptionDate: event.endInscriptionDate ? event.endInscriptionDate.format(DATE_FORMAT) : null,
      status: event.status,
      createDate: event.createDate ? event.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: event.updatedDate ? event.updatedDate.format(DATE_TIME_FORMAT) : null,
      field: event.field,
      tournament: event.tournament,
      season: event.season,
    });

    this.fieldsSharedCollection = this.fieldService.addFieldToCollectionIfMissing(this.fieldsSharedCollection, event.field);
    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      event.tournament
    );
    this.seasonsSharedCollection = this.seasonService.addSeasonToCollectionIfMissing(this.seasonsSharedCollection, event.season);
  }

  protected loadRelationshipsOptions(): void {
    this.tournamentService
      .query({
        sort: ['name'],
      })
      .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
      .pipe(
        map((tournaments: ITournament[]) =>
          this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
        )
      )
      .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));

    this.fieldService
      .query()
      .pipe(map((res: HttpResponse<IField[]>) => res.body ?? []))
      .pipe(map((fields: IField[]) => this.fieldService.addFieldToCollectionIfMissing(fields, this.editForm.get('field')!.value)))
      .subscribe((fields: IField[]) => (this.fieldsSharedCollection = fields));

    this.seasonService
      .query({ 'tournamentId.equals': +localStorage.getItem('TOURNAMENTID')!, 'status.equals': Status.CREATED })
      .pipe(map((res: HttpResponse<ISeason[]>) => res.body ?? []))
      .pipe(map((seasons: ISeason[]) => this.seasonService.addSeasonToCollectionIfMissing(seasons, this.editForm.get('season')!.value)))
      .subscribe((seasons: ISeason[]) => (this.seasonsSharedCollection = seasons));
  }

  protected createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      fromDate: this.editForm.get(['fromDate'])!.value ? dayjs(this.editForm.get(['fromDate'])!.value, DATE_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_FORMAT) : undefined,
      startInscriptionDate: this.editForm.get(['startInscriptionDate'])!.value
        ? dayjs(this.editForm.get(['startInscriptionDate'])!.value, DATE_FORMAT)
        : undefined,
      endInscriptionDate: this.editForm.get(['endInscriptionDate'])!.value
        ? dayjs(this.editForm.get(['endInscriptionDate'])!.value, DATE_FORMAT)
        : undefined,
      endInscriptionPlayersDate: this.editForm.get(['endInscriptionPlayersDate'])!.value
        ? dayjs(this.editForm.get(['endInscriptionPlayersDate'])!.value, DATE_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_FORMAT) : undefined,
      updatedDate: this.editForm.get(['updatedDate'])!.value
        ? dayjs(this.editForm.get(['updatedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      field: this.editForm.get(['field'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
      season: this.editForm.get(['season'])!.value,
    };
  }
}
