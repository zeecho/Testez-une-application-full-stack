import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        // HttpClientModule
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should get user by id', () => {
    const mockUser: User = {
      id: 1,
      email: 'aa@test.com',
      lastName: 'Mevel',
      firstName: 'Fañch',
      admin: true,
      password: 'test123',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const request = httpMock.expectOne('api/user/1');
    expect(request.request.method).toBe('GET');
    request.flush(mockUser);
  });

  it('should delete user by id', () => {
    const mockResponse = { success: true };

    service.delete('1').subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const request = httpMock.expectOne('api/user/1');
    expect(request.request.method).toBe('DELETE');
    request.flush(mockResponse);
  });

  // it('should return a user', () => {
  //   const user = service.getById("0");
  //   expect(user).not.toBeNull();
  //   expect(user).toBeInstanceOf(Observable<User>);
  // })
});
