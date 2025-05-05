import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, appConfig)
  .then(() => {
    // Signal that the bootstrap process is complete
    if (document.readyState === 'complete') {
      // If document is already loaded
      console.log('Application bootstrapped');
    } else {
      // Otherwise wait for document to load
      window.addEventListener('load', () => {
        console.log('Application bootstrapped');
      });
    }
  })
  .catch((err) => console.error(err));
