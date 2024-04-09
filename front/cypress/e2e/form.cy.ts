describe('Form Component', () => {
  beforeEach(() => {
    // Visit login page and log in
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('yoga@studio.com');
    cy.get('input[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    // Wait for successful login and redirection
    cy.url().should('not.include', '/login');
    cy.url().should('include', '/sessions');

    cy.get('button[routerlink="create"]').click();
    cy.url().should('include', '/sessions/create');
  });

  it('should display form fields', () => {
    cy.get('input[formControlName="name"]').should('exist');
    cy.get('input[formControlName="date"]').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').should('exist');
    cy.get('textarea[formControlName="description"]').should('exist');
  });

  it('should disable submit button if form fields are empty', () => {
    cy.get('button[type="submit"]').should('be.disabled');

    // Enter some text in one of the form fields
    cy.get('input[formControlName="name"]').type('Test session');

    // Verify that the submit button is still disabled
    cy.get('button[type="submit"]').should('be.disabled');

    // Fill out other form fields
    cy.get('input[formControlName="date"]').type('2024-04-05');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('.mat-option').first().click();
    cy.get('textarea[formControlName="description"]').type('This is a test description');

    // Verify that the submit button is enabled
    cy.get('button[type="submit"]').should('not.be.disabled');
  });

  it('should navigate back to sessions page after form submission', () => {
    cy.get('input[formControlName="name"]').type('Test session');
    cy.get('input[formControlName="date"]').type('2024-04-05');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('.mat-option').first().click();
    cy.get('textarea[formControlName="description"]').type('This is a test description');
    cy.get('button[type="submit"]').click();

    // Wait for the snackbar message to appear and close
    cy.get('.mat-snack-bar-container').should('be.visible').contains('Session created !');
    cy.wait(3000); // Adjust according to the snackbar duration
    cy.get('.mat-snack-bar-container').should('not.exist');

    // Ensure redirection to sessions page
    cy.url().should('include', '/sessions');
  });

  // // Add more tests as needed
});