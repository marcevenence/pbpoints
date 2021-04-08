import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITournament } from '../tournament.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { TournamentService } from '../service/tournament.service';
import { AccountService } from 'app/core/auth/account.service';
import { TournamentDeleteDialogComponent } from '../delete/tournament-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-tournament',
  templateUrl: './tournament.component.html',
})
export class TournamentComponent implements OnInit {
  currentAccount: any;
  tournaments: ITournament[];
  authSubscription?: Subscription;
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected tournamentService: TournamentService,
    protected accountService: AccountService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.tournaments = [];
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
      this.tournamentService
        .query({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ITournament[]>) => {
            this.isLoading = false;
            this.paginateTournaments(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    } else {
      if (this.currentAccount.authorities.includes('ROLE_OWNER_TOURNAMENT')) {
        this.tournamentService
          .query({
            'ownerId.equals': this.currentAccount.id,
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort(),
          })
          .subscribe(
            (res: HttpResponse<ITournament[]>) => {
              this.isLoading = false;
              this.paginateTournaments(res.body, res.headers);
            },
            () => {
              this.isLoading = false;
            }
          );
      } else {
        if (this.currentAccount.authorities.includes('ROLE_USER')) {
          this.tournamentService
            .query({
              'status.in': ['CREATED', 'IN_PROGRESS'],
              page: this.page,
              size: this.itemsPerPage,
              sort: this.sort(),
            })
            .subscribe(
              (res: HttpResponse<ITournament[]>) => {
                this.isLoading = false;
                this.paginateTournaments(res.body, res.headers);
              },
              () => {
                this.isLoading = false;
              }
            );
        } else {
          this.tournamentService.query({ page: this.page, size: this.itemsPerPage, sort: this.sort() }).subscribe(
            (res: HttpResponse<ITournament[]>) => {
              this.isLoading = false;
              this.paginateTournaments(res.body, res.headers);
            },
            () => {
              this.isLoading = false;
            }
          );
        }
      }
    }
  }

  reset(): void {
    this.page = 0;
    this.tournaments = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.currentAccount = account));
    localStorage.setItem('TOURNAMENTID', '');
    this.loadAll();
  }

  trackId(index: number, item: ITournament): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(tournament: ITournament): void {
    const modalRef = this.modalService.open(TournamentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tournament = tournament;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  Cancel(): void {
    window.history.back();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTournaments(data: ITournament[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.tournaments.push(d);
      }
    }
  }
}
