import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { provideAnimations } from '@angular/platform-browser/animations';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let router: Router;
  let activatedRoute: ActivatedRoute;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
      ],
      declarations: [FormComponent],
      providers: [SessionService, SessionApiService, MatSnackBar, provideAnimations()],
    }).compileComponents();

    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);

    sessionService.sessionInformation = sessionInformation;

    activatedRoute.snapshot.paramMap.get = jest.fn().mockReturnValue('1');

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    router.navigate = jest.fn();

    sessionApiService.update = jest.fn().mockReturnValue(of(session));
    sessionApiService.create = jest.fn().mockReturnValue(of(session));
    sessionApiService.detail = jest.fn();

    matSnackBar.open = jest.fn();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create session', () => {
    const createSpy = jest.spyOn(sessionApiService, 'create');

    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    component.onUpdate = false;
    component.submit();
    expect(createSpy).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
  });

  it('should update session', () => {
    const updateSpy = jest.spyOn(sessionApiService, 'update');
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    component.onUpdate = true;
    component.submit();
    expect(updateSpy).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
  });
});
