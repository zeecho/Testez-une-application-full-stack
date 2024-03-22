import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule, MatSnackBarRef, TextOnlySnackBar } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  
  const mockSession: any = {
    id: 1,
    name: 'session',
    date: new Date(),
    teacher_id: 1,
    description: 'description',
    users: [1],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'lastname',
    firstName: 'firstname',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should participate in session', () => {
    component.sessionId = '1';
    component.userId = '1';
    const participateSpy = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(mockSession));
 
    component.participate();

    expect(participateSpy).toHaveBeenCalledWith('1', '1');
  });

  it('should unparticipate in session', () => {
    const unParticipateSpy = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(mockSession));
    component.sessionId = '1';
    component.userId = '1';

    component.unParticipate();

    expect(unParticipateSpy).toHaveBeenCalledWith('1', '1');
  });

  it('should navigate back', () => {
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
  
    component.back();
  
    expect(backSpy).toHaveBeenCalled();
  });

  it('should show delete button for admin', async() => {

    component.isAdmin = true;
    component.session = mockSession;

    fixture.detectChanges();

    const buttonsDebug = fixture.debugElement.queryAll(By.css('button'));
    const deleteButton = buttonsDebug.find((button) =>
      button.nativeElement.textContent.includes('Delete')
    );

    expect(deleteButton).toBeTruthy();
  });

  it('should not show delete button for non-admin', async () => {
    component.isAdmin = false;
    component.session = mockSession;

    fixture.detectChanges();

    const buttonsDebug = fixture.debugElement.queryAll(By.css('button'));
    const deleteButton = buttonsDebug.find((button) =>
      button.nativeElement.textContent.includes('Delete')
    );

    expect(deleteButton).toBeFalsy();
  });

  it('should delete session and show snack bar', () => {
    component.sessionId = '1';
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
    const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(mockSession));
    const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValueOnce(true);

    component.delete();

    expect(deleteSpy).toHaveBeenCalledWith('1');
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    expect(matSnackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
  });
});

