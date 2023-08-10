import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { of, throwError } from 'rxjs';

import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let router: Router;
  let sessionService: SessionService;
  let authService: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    authService = TestBed.inject(AuthService);

    router.navigate = jest.fn();
    sessionService.logIn = jest.fn().mockReturnValue(of(undefined));
    authService.login = jest.fn().mockReturnValue(of(undefined));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authService login', () => {
    const loginSpy = jest.spyOn(authService, 'login');

    component.submit();

    expect(loginSpy).toHaveBeenCalled;
  });

  it('should call sessionService logIn', () => {
    const logInSpy = jest.spyOn(sessionService, 'logIn');

    component.submit();

    expect(logInSpy).toHaveBeenCalled;
  });

  it('should navigate to /sessions', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.submit();

    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true on login', () => {
    const errorSpy = jest
      .spyOn(authService, 'login')
      .mockReturnValueOnce(throwError(() => new Error()));

    component.submit();

    expect(errorSpy).toHaveBeenCalled;
    expect(component.onError).toBeTruthy();
  });
});
