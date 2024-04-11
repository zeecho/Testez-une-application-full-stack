describe('Creating, showing, editing sessions', () => {
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', { fixture: 'userNonAdmin.json' }).as('userLogin');
    cy.intercept('GET', '/api/user', { fixture: 'userNonAdmin.json' }).as('userData');

    cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher.json' })
    cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' });

    cy.intercept('GET', '/api/session', { fixture: 'sessions.json' })
    cy.intercept('GET', '/api/session/1', { fixture: 'session.json' })

    cy.get('input[formControlName="email"]').type('yoga@studio.com');
    cy.get('input[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.url().should('not.include', '/login');
    cy.url().should('include', '/sessions');
  });

  it('should list sessions correctly', () => {
    cy.get('.items .item').should('have.length', 2);
    cy.get('.items .item').first().should('contain.text', 'Morning Yoga');
    cy.get('.items .item').first().should('contain.text', 'Session on April 12, 2024');
    cy.get('.items .item').first().should('contain.text', 'Join us for a refreshing morning yoga session!');
  });

  it('should show session details when "details" button is clicked', () => {
    cy.get('.items .item').first().contains('button', 'Detail').click();
  
    cy.url().should('include', '/sessions/detail/1');

    cy.get('.mat-card-title').should('contain.text', 'Morning Yoga');
    cy.get('.mat-card-content').should('contain.text', 'Join us for a refreshing morning yoga session!');
  });

  it('should navigate back to sessions page when back button is clicked (details page)', () => {
    cy.get('.items .item').first().contains('button', 'Detail').click();

    cy.get('button').contains('arrow_back').click();

    cy.url().should('include', '/sessions');
  });
});