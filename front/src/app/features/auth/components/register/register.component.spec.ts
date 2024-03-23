import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        RouterTestingModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        AuthService
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.form.value).toEqual({ firstName: '', lastName: '', email: '', password: '' });
  });

  it('should mark form as invalid when email is invalid', () => {
    component.form.patchValue({ firstName: 'John', lastName: 'Doe', email: 'john', password: 'password' });
    expect(component.form.invalid).toBe(true);
  });

  it('should submit form and navigate to login page on successful registration', () => {
    const registerRequest = { firstName: 'John', lastName: 'Doe', email: 'john@example.com', password: 'password' };
    
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(async() => true);
    const authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(of(Promise.resolve(true) as any));

    component.form.patchValue(registerRequest);
    component.submit();

    expect(authServiceSpy).toHaveBeenCalledWith(registerRequest);
    expect(component.onError).toBeFalsy();
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should handle error when registration fails', () => {
    const registerRequest = { firstName: 'John', lastName: 'Doe', email: 'john@example.com', password: 'password' };
    
    const authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error(('Registration failed'))));

    component.form.patchValue(registerRequest);
    component.submit();

    expect(authServiceSpy).toHaveBeenCalledWith(registerRequest);
    expect(component.onError).toBeTruthy();
  });
});
