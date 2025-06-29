import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { MenuComponent } from './menu/menu.component';
import { CartComponent } from './cart/cart.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { KitchenComponent } from './kitchen/kitchen.component';
import { authGuard } from './guards/auth.guard';
import { kitchenGuard } from './guards/kitchen.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'menu', component: MenuComponent },
  { path: 'cart', component: CartComponent, canActivate: [authGuard] },
  { path: 'kitchen', component: KitchenComponent, canActivate: [kitchenGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**', redirectTo: '' },
];
