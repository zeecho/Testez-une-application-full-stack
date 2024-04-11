describe('Creating, showing, editing sessions', () => {
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', { fixture: 'userNonAdmin.json' }).as('userLogin');
    cy.intercept('GET', '/api/user', { fixture: 'userNonAdmin.json' }).as('userData');

    cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher.json' })
    cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' });

    cy.intercept('GET', '/api/session', { fixture: 'sessions.json' })

    cy.get('input[formControlName="email"]').type('yoga@studio.com');
    cy.get('input[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.url().should('not.include', '/login');
    cy.url().should('include', '/sessions');
  });

  it('should not have admin buttons', () => {
    cy.get('button[routerlink="create"]').should('not.exist');
    cy.get('.items .item').first().should('not.contain', 'button', 'Edit');
  })

  it('should participate in a session', () => {
    const sessionData = {
      "id": 1,
      "name": "Morning Yoga",
      "description": "Join us for a refreshing morning yoga session!",
      "date": "2024-04-12T09:00:00Z",
      "teacher_id": 1,
      "users": [1, 2],
      "createdAt": "2024-04-01T12:00:00Z",
      "updatedAt": "2024-04-01T12:00:00Z"
    };

    cy.intercept('GET', '/api/session/1', { body: sessionData }).as('getSession');

    cy.get('.items .item').first().contains('button', 'Detail').click();

    cy.intercept('POST', '/api/session/1/participate/456', { statusCode: 200 }).as('participateSession');

    cy.wait('@getSession');
    cy.get('.ml1').contains('2 attendees');
    cy.get('button').should('contain.text', 'Participate').then( () => {
      sessionData.users.push(456);
      cy.intercept('GET', '/api/session/1', { body: sessionData });
      cy.get('button').contains('Participate').click();
    });

    cy.wait('@participateSession').then((interception) => {
      expect(interception.response.statusCode).to.equal(200);
      cy.get('button').should('contain.text', 'Do not participate')
      cy.get('.ml1').contains('3 attendees');
    });
  });

  it('should unparticipate in a session', () => {
    const sessionData = {
      "id": 1,
      "name": "Morning Yoga",
      "description": "Join us for a refreshing morning yoga session!",
      "date": "2024-04-12T09:00:00Z",
      "teacher_id": 1,
      "users": [1, 2, 456],
      "createdAt": "2024-04-01T12:00:00Z",
      "updatedAt": "2024-04-01T12:00:00Z"
    };
    // Using a session in which the user is considered to be participating in the session
    cy.intercept('GET', '/api/session/1', { body: sessionData }).as('getSession');

    cy.get('.items .item').first().contains('button', 'Detail').click();

    cy.intercept('DELETE', '/api/session/1/participate/456', { statusCode: 200 }).as('unparticipateSession');
    
    cy.wait('@getSession');
    cy.get('.ml1').contains('3 attendees');
    cy.get('button').should('contain.text', 'Do not participate').then( () => {
      sessionData.users.splice(2, 1);
      cy.intercept('GET', '/api/session/1', { body: sessionData });
      cy.get('button').contains('Do not participate').click();
    });

    cy.wait('@unparticipateSession').then((interception) => {
      expect(interception.response.statusCode).to.equal(200);
      cy.get('button').should('contain.text', 'Participate');
      cy.get('.ml1').contains('2 attendees');
    });
  });
});