describe('Custom Test Suite: User Authentication', () => {
  it('should successfully log in', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Workout',
          description: 'A workout session',
          date: '2023-12-30T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      ]
    },);

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
  });

  it('should create a session successfully and display sessions', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        }
      ]
    },);

    cy.intercept('POST', '/api/session', {
      body: {
        id: 2,
        name: 'Workout',
        date: '2023-12-30T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'A workout session'
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Workout',
          description: 'A workout session',
          date: '2023-12-30T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      ]
    },);

    cy.get('button[routerLink=create]').click();
    cy.get('input[formControlName=name]').type(`${"Workout"}`);
    cy.get('input[formControlName=date]').type(`${"2023-12-30"}`);
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type(`${"A workout session"}`);

    cy.get('button[type=submit]').click();
  });

  it('should update a session successfully', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Workout',
        description: 'A workout session',
        date: '2023-12-30T00:00:00.000+00:00',
        teacher_id: 1,
        users: []
      }
    },);

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        }
      ]
    },);

    cy.get('button[data-testid=button_edit]').click();

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'Stretching',
        date: '2023-12-30T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'A stretching session'
      },
    });

    cy.url().should('include', '/sessions/update');

    cy.get('input[formControlName=name]').clear().type(`${"Stretching"}`);
    cy.get('input[formControlName=date]').clear().type(`${"2023-12-30"}`);
    cy.get('textarea[formControlName=description]').clear().type(`${"A stretching session"}`);

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Stretching',
          date: '2023-12-30T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'A stretching session',
          users: []
        }
      ]
    },);

    cy.get('button[type=submit]').click();
  });

  it('should delete a session', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Stretching',
        date: '2023-12-30T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'A stretching session',
        users: []
      }
    },);
    
    cy.intercept('GET', '/api/teacher/1', {
      body: {
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        }
    },);

    cy.get('button[data-testid=button_detail]').click();

    cy.intercept('DELETE', '/api/session/1', {
      status: 200
    },);

    cy.intercept('GET', '/api/session', {
      body: []
    },);

    cy.get('button[data-testid=button_delete]').click();

    cy.url().should('include', '/sessions');
  });
});

describe('Custom Test Suite: User Profile', () => {
  it('should successfully log in', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Workout',
          description: 'A workout session',
          date: '2023-12-30T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      ]
    },);

    cy.get('input[formControlName=email]').type("test@test.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
  });

  it('should check user profile', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/user/1', {
      body: [{
        id: 1,
        firstName: 'Test',
        lastName: 'Test',
        createdAt: '2023-01-30T17:34:44',
        updatedAt: '2023-01-30T17:34:44',
        email: 'yoga@studio.com',
        admin: false
      }]
    },);

    cy.get('[routerLink=me]').click();
    cy.url().should('include', '/me');
      
    cy.intercept('DELETE', '/api/user/1', {
      status: 200
    },);
    cy.get('button[data-testid=button_delete').click();
  });
});

describe('Custom Test Suite: User Registration', () => {
  it('should register successfully', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {status: 200})

    cy.get('input[formControlName=firstName]').type(`${"hello"}`)
    cy.get('input[formControlName=lastName]').type(`${"world"}`)
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/login')
  });
});

describe('Custom Test Suite: User Logout', () => {
  it('should successfully log in', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Workout',
          description: 'A workout session',
          date: '2023-12-30T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      ]
    },);

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
  });

  it('should log out successfully', () => {
    cy.url().should('include', '/sessions');
    cy.get('span[data-testid=button_logout]').click();
  });
});
