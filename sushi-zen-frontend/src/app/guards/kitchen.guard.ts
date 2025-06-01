import { Injectable, inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const kitchenGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  // First check if user is logged in
  if (!authService.isLoggedIn()) {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  // Then check if they have kitchen staff role
  if (authService.isKitchenStaff()) {
    return true;
  }

  // If logged in but not kitchen staff, redirect home
  router.navigate(['/']);
  return false;
};
