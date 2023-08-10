import type { SessionInformation } from '../interfaces/sessionInformation.interface';

import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { Observable } from 'rxjs';

describe('SessionService', () => {
  let service: SessionService;

  const testSessionInformation: SessionInformation = {
    token: '',
    type: '',
    id: 1,
    username: '',
    firstName: '',
    lastName: '',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be isLogged false by default', () => {
    expect(service.isLogged).toBe(false);
  });

  it('should be sessionInformation undefined by default', () => {
    expect(service.sessionInformation).toBe(undefined);
  });

  it('should return the $isLogged Observable', () => {
    expect(service.$isLogged()).toBeInstanceOf(Observable);
  });

  it('should define sessionInformation and set isLogged to true on login', () => {
    service.logIn(testSessionInformation);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toBe(testSessionInformation);
  });

  it('should define sessionInformation and set isLogged to false on logout', () => {
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBe(undefined);
  });
});
