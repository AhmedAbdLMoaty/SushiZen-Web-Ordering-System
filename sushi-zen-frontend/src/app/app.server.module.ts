import { NgModule } from '@angular/core';
import { ServerModule } from '@angular/platform-server';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { routes } from './app.routes';

@NgModule({
  imports: [ServerModule, RouterModule.forRoot(routes), HttpClientModule],
  providers: [],
})
export class AppServerModule {}
