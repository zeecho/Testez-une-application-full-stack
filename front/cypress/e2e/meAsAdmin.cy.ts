describe('Me Component', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', { fixture: 'userAdmin.json' }).as('userLogin');
    cy.intercept('GET', '/api/user/123', { fixture: 'userAdmin.json' }).as('userData');

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
    cy.get('span[routerlink="me"]').click();
  });

  it('should display user information (admin)', () => {
    cy.wait('@userData').then(() => {
      cy.get('h1').should('contain.text', 'User information');
      cy.get('p').contains('Name').should('exist');
      cy.get('p').contains('Email').should('exist');
      cy.get('p').contains('Create at').should('exist');
      cy.get('p').contains('Last update').should('exist');

      cy.get('p').contains('You are admin').should('exist');
      cy.get('button').contains('Delete my account').should('not.exist');
    });
  });

  it('should navigate back when back button is clicked', () => {
    cy.get('button').contains('arrow_back').click();
    cy.url().should('not.include', '/me');
  });
});