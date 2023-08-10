import type { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { AuthService } from './auth.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

describe('AuthService', () => {
  let service: AuthService;
  let httpController: HttpTestingController;

  const pathService = 'api/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login', () => {
    const user = {
      email: '',
      password: '',
    };

    const testSessionInformation: SessionInformation = {
      token: '',
      type: '',
      id: 1,
      username: '',
      firstName: '',
      lastName: '',
      admin: true,
    };

    service.login(user).subscribe((res) => {
      expect(res).toEqual(testSessionInformation);
    });

    const req = httpController.expectOne(`${pathService}/login`);

    expect(req.request.method).toBe('POST');

    req.flush(testSessionInformation);
  });

  it('should register', () => {
    const user = {
      email: '',
      password: '',
      firstName: '',
      lastName: '',
    };

    service.register(user).subscribe((res) => {
      expect(res).toBeTruthy();
    });

    const req = httpController.expectOne(`${pathService}/register`);

    expect(req.request.method).toBe('POST');

    req.flush(true);
  });
});
