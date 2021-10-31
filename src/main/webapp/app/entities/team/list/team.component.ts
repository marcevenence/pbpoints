import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ITeam } from '../team.model';
import { AccountService } from 'app/core/auth/account.service';
import { TeamService } from '../service/team.service';
import { TeamDeleteDialogComponent } from '../delete/team-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { IMainRoster } from 'app/entities/main-roster/main-roster.model';
import { MainRosterService } from 'app/entities/main-roster/service/main-roster.service';

@Component({
  selector: 'jhi-team',
  templateUrl: './team.component.html',
})
export class TeamComponent implements OnInit {
  mainRosters: IMainRoster[];
  currentAccount: any;
  teams?: ITeam[];
  authSubscription?: Subscription;
  isLoading = false;

  constructor(
    protected teamService: TeamService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected accountService: AccountService,
    protected mainRosterService: MainRosterService
  ) {
    this.teams = [];
    this.mainRosters = [];
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.teamService.query().subscribe(
        (res: HttpResponse<ITeam[]>) => {
          this.isLoading = false;
          this.teams = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
      this.mainRosterService.query().subscribe(
        (res: HttpResponse<IMainRoster[]>) => {
          this.isLoading = false;
          this.mainRosters = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    } else {
      this.teamService
        .query({
          'ownerId.equals': this.currentAccount.id,
        })
        .subscribe(
          (res: HttpResponse<ITeam[]>) => {
            this.isLoading = false;
            this.teams = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      this.mainRosterService.query({ 'userExtraId.equals': this.currentAccount.id }).subscribe(
        (res: HttpResponse<IMainRoster[]>) => {
          this.isLoading = false;
          this.mainRosters = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    }
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.currentAccount = account));
    this.loadAll();
  }

  trackId(index: number, item: ITeam): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(team: ITeam): void {
    const modalRef = this.modalService.open(TeamDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.team = team;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  deleteUpd(team: ITeam): void {
    team.active = false;
    this.subscribeToSaveResponse(this.teamService.update(team));
  }

  Cancel(): void {
    window.history.back();
  }

  previousState(): void {
    window.history.back();
  }

  trackMrId(index: number, item: IMainRoster): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveFinalize(): void {
    // Api for inheritance.
  }
}
