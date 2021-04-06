import { IGame } from 'app/entities/game/game.model';
import { IRoster } from 'app/entities/roster/roster.model';
import { IEvent } from 'app/entities/event/event.model';
import { ICategory } from 'app/entities/category/category.model';
import { IFormat } from 'app/entities/format/format.model';

export interface IEventCategory {
  id?: number;
  splitDeck?: boolean | null;
  games?: IGame[] | null;
  rosters?: IRoster[] | null;
  event?: IEvent;
  category?: ICategory;
  format?: IFormat;
}

export class EventCategory implements IEventCategory {
  constructor(
    public id?: number,
    public splitDeck?: boolean | null,
    public games?: IGame[] | null,
    public rosters?: IRoster[] | null,
    public event?: IEvent,
    public category?: ICategory,
    public format?: IFormat
  ) {
    this.splitDeck = this.splitDeck ?? false;
  }
}

export function getEventCategoryIdentifier(eventCategory: IEventCategory): number | undefined {
  return eventCategory.id;
}
