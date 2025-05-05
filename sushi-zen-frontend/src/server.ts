import { APP_BASE_HREF } from '@angular/common';
import express from 'express';
import { fileURLToPath } from 'node:url';
import { dirname, join, resolve } from 'node:path';
import bootstrap from './main.server';
import { readFileSync } from 'node:fs';

// Import the correct SSR module for Angular 19+
import { renderApplication } from '@angular/platform-server';

// The Express app is exported so that it can be used by serverless Functions.
export function app(): express.Express {
  const server = express();
  const serverDistFolder = dirname(fileURLToPath(import.meta.url));
  const browserDistFolder = resolve(serverDistFolder, '../browser');
  const indexHtml = join(browserDistFolder, 'index.html');

  // Setup server-side rendering
  server.set('view engine', 'html');
  server.set('views', browserDistFolder);

  // Serve static files from /browser
  server.get(
    '*.*',
    express.static(browserDistFolder, {
      maxAge: '1y',
    })
  );

  // All regular routes use the Universal engine
  server.get('*', (req, res, next) => {
    const { protocol, originalUrl, baseUrl, headers } = req;
    const url = `${protocol}://${headers.host}${originalUrl}`;

    // Read index.html
    const indexHtmlContent = readFileSync(indexHtml, 'utf8');

    // Render the app
    renderApplication(bootstrap, {
      document: indexHtmlContent,
      url: url,
      platformProviders: [{ provide: APP_BASE_HREF, useValue: baseUrl }],
    })
      .then((html) => {
        res.send(html);
        // Force garbage collection to clean up resources
        if (global.gc) {
          global.gc();
        }
      })
      .catch((err) => {
        console.error('Error rendering application:', err);
        res.status(500).send('Server error');
        next(err);
      });
  });

  return server;
}

function run(): void {
  const port = process.env['PORT'] || 4000;

  // Start up the Node server
  const server = app();
  server.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

// If this file is called directly, run the server
if (import.meta.url === `file://${process.argv[1]}`) {
  run();
}
