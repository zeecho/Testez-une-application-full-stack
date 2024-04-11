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
    cy.intercept('GET', '/api/session/1', { fixture: 'session.json' })

    cy.get('.items .item').first().contains('button', 'Detail').click();

    cy.intercept('POST', '/api/session/1/participate/456', { statusCode: 200 }).as('participateSession');
    
    cy.get('button').should('contain.text', 'Participate');

    cy.get('button').contains('Participate').click();

    cy.wait('@participateSession').then((interception) => {
      expect(interception.response.statusCode).to.equal(200);
    });
  });

  it('should unparticipate in a session', () => {
    // Using a session in which the user is considered to be participating in the session
    cy.intercept('GET', '/api/session/1', { fixture: 'session2.json' })

    cy.get('.items .item').first().contains('button', 'Detail').click();

    cy.intercept('POST', '/api/session/1/participate/456', { statusCode: 200 }).as('unparticipateSession');

    cy.get('button').should('contain.text', 'Do not participate');

    cy.get('button').contains('Do not participate').click();
  });
});