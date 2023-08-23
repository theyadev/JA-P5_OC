import type { Teacher } from '../interfaces/teacher.interface';

import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpController: HttpTestingController;

  const pathService = 'api/teacher';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeacherService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all teachers', () => {
    const teachers: Teacher[] = [
      {
        id: 1,
        firstName: 'Teacher 1',
        lastName: '',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        firstName: 'Teacher 2',
        lastName: '',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    service.all().subscribe((data) => {
      expect(data).toEqual(teachers);
    });

    const req = httpController.expectOne(pathService);

    expect(req.request.method).toEqual('GET');

    req.flush(teachers);
  });

  it('should get teacher by id', () => {
    const teacherId = '1';
    const teacher: Teacher = {
      id: 1,
      firstName: 'Teacher 1',
      lastName: '',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.detail(teacherId).subscribe((data) => {
      expect(data).toEqual(teacher);
    });

    const req = httpController.expectOne(`${pathService}/${teacherId}`);

    expect(req.request.method).toEqual('GET');

    req.flush(teacher);
  });
});
