import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeamPoint, TeamPoint } from '../team-point.model';
import { TeamPointService } from '../service/team-point.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

@Component({
  selector: 'jhi-team-point-update',
  templateUrl: './team-point-update.component.html',
})
export class TeamPointUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    team: [null, Validators.required],
    tournament: [null, Validators.required],
  });

  constructor(
    protected teamPointService: TeamPointService,
    protected teamService: TeamService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamPoint }) => {
      this.updateForm(teamPoint);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teamPoint = this.createFromForm();
    if (teamPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.teamPointService.update(teamPoint));
    } else {
      this.subscribeToSaveResponse(this.teamPointService.create(teamPoint));
    }
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeamPoint>>): void {
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

  protected updateForm(teamPoint: ITeamPoint): void {
    this.editForm.patchValue({
      id: teamPoint.id,
      points: teamPoint.points,
      team: teamPoint.team,
      tournament: teamPoint.tournament,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, teamPoint.team);
    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      teamPoint.tournament
    );
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.tournamentService
      .query()
      .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
      .pipe(
        map((tournaments: ITournament[]) =>
          this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
        )
      )
      .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
  }

  protected createFromForm(): ITeamPoint {
    return {
      ...new TeamPoint(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      team: this.editForm.get(['team'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
    };
  }
}
