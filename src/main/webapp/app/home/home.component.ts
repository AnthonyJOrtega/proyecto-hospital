import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);

  private readonly destroy$ = new Subject<void>();

  private readonly accountService = inject(AccountService);
  private readonly router = inject(Router);

  private chatlingScriptLoaded = false;

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account.set(account);
        if (account && !this.chatlingScriptLoaded) {
          this.addChatlingScript();
          this.chatlingScriptLoaded = true;
        }
        if (!account && this.chatlingScriptLoaded) {
          this.removeChatlingScript();
          this.chatlingScriptLoaded = false;
        }
      });
  }

  addChatlingScript(): void {
    // Configuración del chatbot
    const config = document.createElement('script');
    config.innerHTML = 'window.chtlConfig = { chatbotId: "1255511581" }';
    config.id = 'chtl-config';
    document.body.appendChild(config);

    // Script del chatbot
    const script = document.createElement('script');
    script.id = 'chtl-script';
    script.type = 'text/javascript';
    script.async = true;
    script.setAttribute('data-id', '1255511581');
    script.src = 'https://chatling.ai/js/embed.js';
    document.body.appendChild(script);
  }

  removeChatlingScript(): void {
    const script = document.getElementById('chtl-script');
    if (script) {
      script.remove();
    }
    const config = document.getElementById('chtl-config');
    if (config) {
      config.remove();
    }
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  register(): void {
    this.router.navigate(['/account/register']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  isAuthenticated(): boolean {
    return this.account() !== null;
  }
}
