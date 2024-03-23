import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [AuthService, SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    jest.spyOn(sessionService, 'logIn').mockImplementation(() => {});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.form.value).toEqual({ email: '', password: '' });
  });

  it('should submit form and log in on successful authentication', () => {
    const loginResponse: SessionInformation = {
      token: "token",
      type: "type",
      id: 1,
      username: "test@test.com",
      firstName: "Fañch",
      lastName: "Mevel",
      admin: false 
    };
    const loginRequest = { email: 'test@test.com', password: 'password' };
    
    jest.spyOn(authService, 'login').mockReturnValue(of(loginResponse));
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(async() => true);
    
    component.form.patchValue(loginRequest);
    component.submit();

    expect(authService.login).toHaveBeenCalledWith(loginRequest);
    expect(sessionService.logIn).toHaveBeenCalled();
    expect(component.onError).toBeFalsy();
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should handle error when authentication fails', () => {
    const loginRequest = { email: 'test@test.com', password: 'password' };

    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Authentication failed')));

    component.form.patchValue(loginRequest);
    component.submit();

    expect(authService.login).toHaveBeenCalledWith(loginRequest);
    expect(component.onError).toBeTruthy();
  });
});
