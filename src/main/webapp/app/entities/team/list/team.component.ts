import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { ITeam } from '../team.model';
import { AccountService } from 'app/core/auth/account.service';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { TeamService } from '../service/team.service';
import { TeamDeleteDialogComponent } from '../delete/team-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-team',
  templateUrl: './team.component.html',
})
export class TeamComponent implements OnInit {
  currentAccount: any;
  isSaving = false;
  teams?: ITeam[];
  authSubscription?: Subscription;
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate!: string;
  ascending!: boolean;

  constructor(
    protected teamService: TeamService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected accountService: AccountService
  ) {
    this.teams = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.teamService
        .query({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ITeam[]>) => {
            this.isLoading = false;
            this.paginateTeams(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    } else {
      this.teamService
        .query({
          'ownerId.equals': this.currentAccount.id,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ITeam[]>) => {
            this.isLoading = false;
            this.paginateTeams(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    }
  }

  reset(): void {
    this.page = 0;
    this.teams = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.currentAccount = account));
    this.loadAll();
  }

  trackId(index: number, item: ITeam): number {
    return item.id!;
  }

  delete(team: ITeam): void {
    const modalRef = this.modalService.open(TeamDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.team = team;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTeams(data: ITeam[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.teams?.push(d);
      }
    }
  }
  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
