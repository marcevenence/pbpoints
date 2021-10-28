jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SponsorService } from '../service/sponsor.service';
import { ISponsor, Sponsor } from '../sponsor.model';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

import { SponsorUpdateComponent } from './sponsor-update.component';

describe('Component Tests', () => {
  describe('Sponsor Management Update Component', () => {
    let comp: SponsorUpdateComponent;
    let fixture: ComponentFixture<SponsorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sponsorService: SponsorService;
    let tournamentService: TournamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SponsorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SponsorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SponsorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sponsorService = TestBed.inject(SponsorService);
      tournamentService = TestBed.inject(TournamentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Tournament query and add missing value', () => {
        const sponsor: ISponsor = { id: 456 };
        const tournament: ITournament = { id: 54173 };
        sponsor.tournament = tournament;

        const tournamentCollection: ITournament[] = [{ id: 18619 }];
        spyOn(tournamentService, 'query').and.returnValue(of(new HttpResponse({ body: tournamentCollection })));
        const additionalTournaments = [tournament];
        const expectedCollection: ITournament[] = [...additionalTournaments, ...tournamentCollection];
        spyOn(tournamentService, 'addTournamentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ sponsor });
        comp.ngOnInit();

        expect(tournamentService.query).toHaveBeenCalled();
        expect(tournamentService.addTournamentToCollectionIfMissing).toHaveBeenCalledWith(tournamentCollection, ...additionalTournaments);
        expect(comp.tournamentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const sponsor: ISponsor = { id: 456 };
        const tournament: ITournament = { id: 80570 };
        sponsor.tournament = tournament;

        activatedRoute.data = of({ sponsor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sponsor));
        expect(comp.tournamentsSharedCollection).toContain(tournament);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sponsor = { id: 123 };
        spyOn(sponsorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sponsor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sponsor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sponsorService.update).toHaveBeenCalledWith(sponsor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sponsor = new Sponsor();
        spyOn(sponsorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sponsor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sponsor }));
        saveSubject.complete();

        // THEN
        expect(sponsorService.create).toHaveBeenCalledWith(sponsor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sponsor = { id: 123 };
        spyOn(sponsorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sponsor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sponsorService.update).toHaveBeenCalledWith(sponsor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTournamentById', () => {
        it('Should return tracked Tournament primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTournamentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
