describe('Register spec', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should display all input fields', () => {
    cy.get('input[formControlName="firstName"]').should('exist');
    cy.get('input[formControlName="lastName"]').should('exist');
    cy.get('input[formControlName="email"]').should('exist');
    cy.get('input[formControlName="password"]').should('exist');
  });

  it('should navigate to login page on successful registration', () => {
    cy.server();
    cy.route('POST', '/api/auth/register', {}).as('register');

    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName="firstName"]').type('John');

    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('password');

    cy.get('button[type="submit"]').should('not.be.disabled');

    cy.get('button[type="submit"]').click();

    cy.wait('@register').then(() => {
      cy.url().should('include', '/login');
    });
  });
});
