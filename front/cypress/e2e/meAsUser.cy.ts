describe('Me Component', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', { fixture: 'userNonAdmin.json' }).as('userLogin');
    cy.intercept('GET', '/api/user/456', { fixture: 'userNonAdmin.json' }).as('userData');

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
    cy.get('span[routerlink="me"]').click();
  });

  it('should display user information (non admin)', () => {
    cy.wait('@userData').then(() => {
      cy.get('h1').should('contain.text', 'User information');
      cy.get('p').contains('Name').should('exist');
      cy.get('p').contains('Email').should('exist');
      cy.get('p').contains('Create at').should('exist');
      cy.get('p').contains('Last update').should('exist');

      cy.get('p').contains('You are admin').should('not.exist');
      cy.get('p').contains('Delete my account').should('exist');
    });
  });

  // it('should allow user to delete account', () => {
  //   cy.intercept('DELETE', '/api/user/456').as('deleteUser');

  //   cy.wait(['@userData']).then(() => {
  //     cy.get('button').contains('Detail').click();
  //     cy.wait('@deleteUser').then(() => {
  //       cy.get('.mat-snack-bar-container').should('contain.text', 'Your account has been deleted');
  //       cy.url().should('include', '/');
  //     });
  //   });
  // });

  it('should navigate back when back button is clicked', () => {
    cy.get('button').contains('arrow_back').click();
    cy.url().should('not.include', '/me');
  });
});