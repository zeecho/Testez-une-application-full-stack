import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'session 1',
      date: new Date(),
      teacher_id: 1,
      description: 'description',
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      name: 'session 2',
      date: new Date(),
      teacher_id: 1,
      description: 'description',
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('GET');
    req.flush(mockSessions);
  });

  it('should retrieve session details by ID', () => {

    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSessions[0]);
    });

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toEqual('GET');
    req.flush(mockSessions[0]);
  });

  it('should delete a session by ID', () => {
    const sessionId = '1';

    service.delete(sessionId).subscribe();

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush({});
  });

  it('should create a session', () => {
    const newSession: Session = {
      id: 3,
      name: 'session 3',
      date: new Date(),
      teacher_id: 1,
      description: 'description',
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.create(newSession).subscribe(session => {
      expect(session).toEqual(newSession);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('POST');
    req.flush(newSession);
  });

  it('should update a session by ID', () => {
    const sessionId = '1';
    const updatedSession = {
      ...mockSessions[0],
      name: 'Updated Session'
    }
    service.update(sessionId, updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toEqual('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should allow a user to participate in a session', () => {
    const sessionId = '1';
    const userId = 'user1';

    service.participate(sessionId, userId).subscribe();

    const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toEqual('POST');
    req.flush({});
  });
  
  it('should allow a user to unparticipate in a session', () => {
    const sessionId = '1';
    const userId = 'user1';

    service.unParticipate(sessionId, userId).subscribe();

    const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush({});
  });
});
