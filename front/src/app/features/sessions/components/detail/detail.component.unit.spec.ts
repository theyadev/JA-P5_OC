import type { Teacher } from 'src/app/interfaces/teacher.interface';

import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let router: Router;
  let activatedRoute: ActivatedRoute;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;

  const session: Session = {
    id: 1,
    users: [2],
    teacher_id: 2,
    name: 'test',
    description: 'test',
    date: new Date(),
  };

  const sessionInformation = {
    token: '',
    type: '',
    id: 1,
    username: '',
    firstName: '',
    lastName: '',
    admin: true,
  };

  const teacher: Teacher = {
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
      providers: [
        SessionService,
        SessionApiService,
        TeacherService,
        MatSnackBar,
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);

    sessionService.sessionInformation = sessionInformation;

    activatedRoute.snapshot.paramMap.get = jest.fn().mockReturnValue('1');

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    router.navigate = jest.fn();

    sessionApiService.delete = jest.fn();
    sessionApiService.detail = jest.fn();
    sessionApiService.participate = jest.fn();
    sessionApiService.unParticipate = jest.fn().mockReturnValue(of(session));

    teacherService.detail = jest.fn();

    matSnackBar.open = jest.fn();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call windows history back', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(backSpy).toHaveBeenCalledTimes(1);
  });

  it('should call sessionApiService delete and navigate to /sessions on delete', () => {
    const sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'delete')
      .mockReturnValue(of(null));

    component.delete();

    expect(sessionApiServiceSpy).toHaveBeenCalledWith('1');
    expect(matSnackBar.open).toHaveBeenCalledWith(
      'Session deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call sessionApiService participate', () => {
    const participeSpy = jest
      .spyOn(sessionApiService, 'participate')
      .mockReturnValue(of(void 0));

    component.participate();

    expect(participeSpy).toHaveBeenCalledWith(
      session.id?.toString(),
      sessionInformation.id.toString()
    );
  });

  it('should call sessionApiService unParticipate', () => {
    const unParticipeSpy = jest
      .spyOn(sessionApiService, 'unParticipate')
      .mockReturnValue(of(void 0));

    component.unParticipate();

    expect(unParticipeSpy).toHaveBeenCalledWith(
      session.id?.toString(),
      sessionInformation.id.toString()
    );
  });

  it('should call sessionApiService & teacherService detail on fetchSession', () => {
    const SessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'detail')
      .mockReturnValue(of(session));
    const TeacherServiceSpy = jest
      .spyOn(teacherService, 'detail')
      .mockReturnValue(of(teacher));

    component.ngOnInit();

    expect(SessionApiServiceSpy).toHaveBeenCalledWith('1');
    expect(TeacherServiceSpy).toHaveBeenCalledWith('2');
    expect(component.session).toEqual(session);
    expect(component.teacher).toEqual(teacher);
    expect(component.isParticipate).toBeFalsy();
  });
});
