src\app\app.server.module.ts
import { NgModule } from '@angular/core';
import { ServerModule } from '@angular/platform-server';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    ServerModule,
    RouterModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: []
  // No bootstrap array needed - AppComponent is standalone
})
export class AppServerModule {}