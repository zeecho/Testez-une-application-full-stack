import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule,
        ReactiveFormsModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      providers: [
        {
          provide: SessionService,
          useValue: {
            sessionInformation: mockSessionService.sessionInformation,
            logOut: jest.fn()
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jest.fn()
          }
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate back', () => {
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
  
    component.back();
  
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete user and navigate to home', () => {
    const deleteUserSpy = jest.spyOn(userService, 'delete').mockReturnValue(of(null));
    const logoutSpy = jest.spyOn(sessionService, 'logOut');
    const snackBarOpenSpy = jest.spyOn(matSnackBar, 'open');
    const routerNavigateSpy = jest.spyOn(router, 'navigate');

    component.delete();

    expect(deleteUserSpy).toHaveBeenCalledWith('1');
    expect(snackBarOpenSpy).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(logoutSpy).toHaveBeenCalled();
    expect(routerNavigateSpy).toHaveBeenCalledWith(['/']);
  });
});
