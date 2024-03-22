import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockUser: SessionInformation = {
    token: "token",
    type: "type",
    id: 1,
    username: "aa@test.com",
    firstName: "Fañch",
    lastName: "Mevel",
    admin: true
  };

  const mockSession: any = {
    id: 1,
    name: 'test',
    date: new Date(),
    teacher_id: 1,
    description: 'test desc',
    createdAt: new Date(),
    updatedAt: new Date(),
    users: [ 1, 2 ]
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        RouterTestingModule.withRoutes([{ path: 'update', component: FormComponent }]),
        ReactiveFormsModule,
        MatSnackBarModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: { sessionInformation: mockUser } },
      ]
    }).compileComponents();


    fixture = TestBed.createComponent(FormComponent);
    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to sessions if user is not admin', () => {
    const navigateSpy = jest.spyOn(router,'navigate').mockImplementation(async() => true);
    const alternativeMockUser = {
      ...mockUser,
      admin: false
    }
    sessionService.sessionInformation = alternativeMockUser;

    component.ngOnInit();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should create session and show snack bar message when not updating', () => {  
    component.onUpdate = false;

    const createSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(async() => true);
  
    component.submit();
  
    expect(createSpy).toHaveBeenCalled();
    expect(matSnackBarSpy).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });
  
  it('should update session and show snack bar message when updating', () => {
    component.onUpdate = true;
  
    const updateSpy = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockSession));
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(async() => true);
  
    component.submit();
  
    expect(updateSpy).toHaveBeenCalled();
    expect(matSnackBarSpy).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });
});
