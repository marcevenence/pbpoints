import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'bracket',
        data: { pageTitle: 'pbpointsApp.bracket.home.title' },
        loadChildren: () => import('./bracket/bracket.module').then(m => m.BracketModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'pbpointsApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'city',
        data: { pageTitle: 'pbpointsApp.city.home.title' },
        loadChildren: () => import('./city/city.module').then(m => m.CityModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'pbpointsApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'doc-type',
        data: { pageTitle: 'pbpointsApp.docType.home.title' },
        loadChildren: () => import('./doc-type/doc-type.module').then(m => m.DocTypeModule),
      },
      {
        path: 'event',
        data: { pageTitle: 'pbpointsApp.event.home.title' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'event-category',
        data: { pageTitle: 'pbpointsApp.eventCategory.home.title' },
        loadChildren: () => import('./event-category/event-category.module').then(m => m.EventCategoryModule),
      },
      {
        path: 'format',
        data: { pageTitle: 'pbpointsApp.format.home.title' },
        loadChildren: () => import('./format/format.module').then(m => m.FormatModule),
      },
      {
        path: 'formula',
        data: { pageTitle: 'pbpointsApp.formula.home.title' },
        loadChildren: () => import('./formula/formula.module').then(m => m.FormulaModule),
      },
      {
        path: 'game',
        data: { pageTitle: 'pbpointsApp.game.home.title' },
        loadChildren: () => import('./game/game.module').then(m => m.GameModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'pbpointsApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'player',
        data: { pageTitle: 'pbpointsApp.player.home.title' },
        loadChildren: () => import('./player/player.module').then(m => m.PlayerModule),
      },
      {
        path: 'player-detail-point',
        data: { pageTitle: 'pbpointsApp.playerDetailPoint.home.title' },
        loadChildren: () => import('./player-detail-point/player-detail-point.module').then(m => m.PlayerDetailPointModule),
      },
      {
        path: 'player-point',
        data: { pageTitle: 'pbpointsApp.playerPoint.home.title' },
        loadChildren: () => import('./player-point/player-point.module').then(m => m.PlayerPointModule),
      },
      {
        path: 'province',
        data: { pageTitle: 'pbpointsApp.province.home.title' },
        loadChildren: () => import('./province/province.module').then(m => m.ProvinceModule),
      },
      {
        path: 'roster',
        data: { pageTitle: 'pbpointsApp.roster.home.title' },
        loadChildren: () => import('./roster/roster.module').then(m => m.RosterModule),
      },
      {
        path: 'team',
        data: { pageTitle: 'pbpointsApp.team.home.title' },
        loadChildren: () => import('./team/team.module').then(m => m.TeamModule),
      },
      {
        path: 'team-detail-point',
        data: { pageTitle: 'pbpointsApp.teamDetailPoint.home.title' },
        loadChildren: () => import('./team-detail-point/team-detail-point.module').then(m => m.TeamDetailPointModule),
      },
      {
        path: 'team-point',
        data: { pageTitle: 'pbpointsApp.teamPoint.home.title' },
        loadChildren: () => import('./team-point/team-point.module').then(m => m.TeamPointModule),
      },
      {
        path: 'tournament',
        data: { pageTitle: 'pbpointsApp.tournament.home.title' },
        loadChildren: () => import('./tournament/tournament.module').then(m => m.TournamentModule),
      },
      {
        path: 'user-extra',
        data: { pageTitle: 'pbpointsApp.userExtra.home.title' },
        loadChildren: () => import('./user-extra/user-extra.module').then(m => m.UserExtraModule),
      },
      {
        path: 'suspension',
        data: { pageTitle: 'pbpointsApp.suspension.home.title' },
        loadChildren: () => import('./suspension/suspension.module').then(m => m.SuspensionModule),
      },
      {
        path: 'main-roster',
        data: { pageTitle: 'pbpointsApp.main-roster.home.title' },
        loadChildren: () => import('./main-roster/main-roster.module').then(m => m.MainRosterModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
