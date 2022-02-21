import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-help',
  templateUrl: './help.component.html',
})
export class HelpComponent implements OnInit {
  loadAll(): void {
    // Hacer que hace algo
  }

  ngOnInit(): void {
    this.loadAll();
  }

  Cancel(): void {
    window.history.back();
  }
}
