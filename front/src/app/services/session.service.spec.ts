import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { BehaviorSubject } from 'rxjs';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log the user in', () => {
    const mockUser: SessionInformation = {
      token: "token",
      type: "type",
      id: 1,
      username: "aa@test.com",
      firstName: "Fañch",
      lastName: "Mevel",
      admin: false
    };

    expect(service.isLogged).toBeFalsy();

    service.logIn(mockUser);

    expect(service.sessionInformation).toEqual(mockUser);
    expect(service.isLogged).toBeTruthy();
  });

  it('should log the user out', () => {
    const mockUser: SessionInformation = {
      token: "token",
      type: "type",
      id: 1,
      username: "aa@test.com",
      firstName: "Fañch",
      lastName: "Mevel",
      admin: false
    };

    service.isLogged = true;
    service.sessionInformation = mockUser;
    service.logOut();
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();
  });

  // TODO This doesn't really work
  it('should return the status of isLogged as an Observable and be true', () => {
    const mockUser: SessionInformation = {
      token: "token",
      type: "type",
      id: 1,
      username: "aa@test.com",
      firstName: "Fañch",
      lastName: "Mevel",
      admin: false
    };
    service.logIn(mockUser);
    
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBeTruthy();
    });
  })
});
