import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT, DATE_FORMAT } from 'app/config/input.constants';

import { IEvent, Event } from '../event.model';
import { EventService } from '../service/event.service';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;

  citiesSharedCollection: ICity[] = [];
  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    fromDate: [],
    endDate: [],
    endInscriptionDate: [],
    status: [],
    createDate: [],
    updatedDate: [],
    city: [],
    tournament: [],
  });

  constructor(
    protected eventService: EventService,
    protected cityService: CityService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

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

  trackCityById(index: number, item: ICity): number {
    return item.id!;
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
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
      endInscriptionDate: event.endInscriptionDate ? event.endInscriptionDate.format(DATE_FORMAT) : null,
      status: event.status,
      createDate: event.createDate ? event.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: event.updatedDate ? event.updatedDate.format(DATE_TIME_FORMAT) : null,
      city: event.city,
      tournament: event.tournament,
    });

    this.citiesSharedCollection = this.cityService.addCityToCollectionIfMissing(this.citiesSharedCollection, event.city);
    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      event.tournament
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cityService
      .query({
        sort: ['name,asc'],
      })
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing(cities, this.editForm.get('city')!.value)))
      .subscribe((cities: ICity[]) => (this.citiesSharedCollection = cities));

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
  }

  protected createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      fromDate: this.editForm.get(['fromDate'])!.value ? dayjs(this.editForm.get(['fromDate'])!.value, DATE_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_FORMAT) : undefined,
      endInscriptionDate: this.editForm.get(['endInscriptionDate'])!.value
        ? dayjs(this.editForm.get(['endInscriptionDate'])!.value, DATE_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_FORMAT) : undefined,
      updatedDate: this.editForm.get(['updatedDate'])!.value
        ? dayjs(this.editForm.get(['updatedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      city: this.editForm.get(['city'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
    };
  }
}
