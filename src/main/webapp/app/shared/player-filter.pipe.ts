import { Pipe, PipeTransform } from '@angular/core';
import { IPlayer } from 'app/entities/player/player.model';

@Pipe({
  name: 'filterByPlayerProfile',
})
export class PlayerFilterPipe implements PipeTransform {
  transform(items: IPlayer[], profileName: string): Array<any> {
    return items.filter(item => item.profile === profileName);
  }
}
