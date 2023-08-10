import type { User } from '../interfaces/user.interface';

import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpController: HttpTestingController;

  const pathService = 'api/user';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(UserService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {
    const userId = '1';
    const user: User = {
      id: 1,
      email: '',
      firstName: '',
      lastName: '',
      admin: false,
      createdAt: new Date(),
      password: '',
    };

    service.getById(userId).subscribe((data) => {
      expect(data).toEqual(user);
    });

    const req = httpController.expectOne(`${pathService}/${userId}`);

    expect(req.request.method).toEqual('GET');

    req.flush(user);
  });

  it('should delete user', () => {
    const userId = '1';

    service.delete(userId).subscribe();

    const req = httpController.expectOne(`${pathService}/${userId}`);

    expect(req.request.method).toEqual('DELETE');

    req.flush({});
  });
});
