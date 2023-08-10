import type { Session } from '../interfaces/session.interface';

import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpController: HttpTestingController;

  const pathService = 'api/session';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(SessionApiService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sessions', () => {
    const testSessions: Session[] = [
      {
        id: 1,
        name: 'Session 1',
        description: 'Description 1',
        date: new Date(),
        teacher_id: 1,
        users: [1],
      },
      {
        id: 2,
        name: 'Session 2',
        description: 'Description 2',
        date: new Date(),
        teacher_id: 2,
        users: [2],
      },
    ];

    service.all().subscribe((sessions) => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(testSessions);
    });

    const req = httpController.expectOne(pathService);

    expect(req.request.method).toBe('GET');

    req.flush(testSessions);
  });

  it('should get session by id', () => {
    const testSessionId = '1';
    const testSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [1],
    };

    service.detail(testSessionId).subscribe((session) => {
      expect(session).toEqual(testSession);
    });

    const req = httpController.expectOne(`${pathService}/${testSessionId}`);

    expect(req.request.method).toBe('GET');

    req.flush(testSession);
  });

  it('should create session', () => {
    const testSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [1],
    };

    service.create(testSession).subscribe((session) => {
      expect(session).toEqual(testSession);
    });

    const req = httpController.expectOne(pathService);

    expect(req.request.method).toBe('POST');

    req.flush(testSession);
  });

  it('should update session', () => {
    const testSessionId = '1';
    const testSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [1],
    };

    service.update(testSessionId, testSession).subscribe((session) => {
      expect(session).toEqual(testSession);
    });

    const req = httpController.expectOne(`${pathService}/${testSessionId}`);

    expect(req.request.method).toBe('PUT');

    req.flush(testSession);
  });

  it('should participate in session', () => {
    const testSessionId = '1';
    const testUserId = '1';

    service.participate(testSessionId, testUserId).subscribe((session) => {
      expect(session).toBeTruthy();
    });

    const req = httpController.expectOne(
      `${pathService}/${testSessionId}/participate/${testUserId}`
    );

    expect(req.request.method).toBe('POST');

    req.flush({});
  });

  it('should unparticipate in session', () => {
    const testSessionId = '1';
    const testUserId = '1';

    service.unParticipate(testSessionId, testUserId).subscribe((session) => {
      expect(session).toBeTruthy();
    });

    const req = httpController.expectOne(
      `${pathService}/${testSessionId}/participate/${testUserId}`
    );

    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });
});
