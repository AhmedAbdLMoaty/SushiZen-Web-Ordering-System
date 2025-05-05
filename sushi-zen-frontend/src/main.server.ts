import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { config } from './app/app.config.server';

// Export a function that returns a Promise of the bootstrapped application
const bootstrap = () => bootstrapApplication(AppComponent, config);

export default bootstrap;
