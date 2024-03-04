// import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        // HttpClientModule
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all teachers', () => {
    const mockTeachers: Teacher[] = [
      { id: 1, lastName: 'Mevel', firstName: 'Fañch', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, lastName: 'Kalvez', firstName: 'Alan', createdAt: new Date(), updatedAt: new Date() }
    ];
    service.all().subscribe(teachers => {
      expect(teachers.length).toBe(2);
      expect(teachers).toEqual(mockTeachers);
    });

    const request = httpMock.expectOne('api/teacher');
    expect(request.request.method).toBe('GET');
    request.flush(mockTeachers);
  });

  it('should retrieve teacher details by id', () => {
    const mockTeacher: Teacher = { 
      id: 1, 
      lastName: 'Mevel', 
      firstName: 'Fañch', 
      createdAt: new Date(), 
      updatedAt: new Date() 
    };

    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    });

    const request = httpMock.expectOne('api/teacher/1');
    expect(request.request.method).toBe('GET');
    request.flush(mockTeacher);
  });
});
