describe('Creating, showing, editing sessions', () => {
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', { fixture: 'userAdmin.json' }).as('userLogin');
    cy.intercept('GET', '/api/user', { fixture: 'userAdmin.json' }).as('userData');

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

  it('should allow deleting a session when "Delete" button is clicked', () => {
    cy.intercept('DELETE', '/api/session/1', { statusCode: 200 }).as('deleteSession');

    cy.get('.items .item').first().contains('button', 'Detail').click();

    cy.contains('button', 'Delete').click();

    cy.wait('@deleteSession').then((interception) => {
      expect(interception.response.statusCode).to.equal(200);
    });

    cy.url().should('include', '/sessions');
  });

  it('should allow editing session when "edit" button is clicked', () => {
    cy.get('.items .item').first().contains('button', 'Edit').click();
  
    cy.url().should('include', '/sessions/update/1');

    cy.get('input[formControlName="name"]').should('have.value', 'Morning Yoga');
    cy.get('textarea[formControlName="description"]').should('have.value', 'Join us for a refreshing morning yoga session!');
  
    // Modify session details
    cy.get('input[formControlName="name"]').clear().type('Updated Morning Yoga');
    cy.get('textarea[formControlName="description"]').clear().type('Join us for an updated morning yoga session!');
  
    cy.intercept('PUT', '/api/session/1', (req) => {
      expect(req.body).to.have.property('name', 'Updated Morning Yoga');
      expect(req.body).to.have.property('date', '2024-04-12');
      expect(req.body).to.have.property('teacher_id').that.is.a('number');
      expect(req.body).to.have.property('description', 'Join us for an updated morning yoga session!');
      req.reply({ statusCode: 200, body: { id: 1 } }); // Mock a successful response
    }).as('updateSession');

    cy.get('button[type="submit"]').click();
  
    cy.url().should('include', '/sessions');
    cy.get('.mat-snack-bar-container').should('be.visible').contains('Session updated !');
  });

  it('should list sessions correctly', () => {
    cy.get('.items .item').should('have.length', 2);
    cy.get('.items .item').first().should('contain.text', 'Morning Yoga');
    cy.get('.items .item').first().should('contain.text', 'Session on April 12, 2024');
    cy.get('.items .item').first().should('contain.text', 'Join us for a refreshing morning yoga session!');
  });

  it('should navigate back to sessions page when back button is clicked (details page)', () => {
    cy.get('.items .item').first().contains('button', 'Detail').click();

    cy.get('button').contains('arrow_back').click();

    cy.url().should('include', '/sessions');
  });

  it('should navigate back to sessions page when back button is clicked (creation form)', () => {
    cy.get('button[routerlink="create"]').click();
    cy.url().should('include', '/sessions/create');

    cy.get('button').contains('arrow_back').click();

    cy.url().should('include', '/sessions');
  });

  it('should display creation form fields', () => {
    cy.get('button[routerlink="create"]').click();
    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName="name"]').should('exist');
    cy.get('input[formControlName="date"]').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').should('exist');
    cy.get('textarea[formControlName="description"]').should('exist');
  });

  it('should disable submit button if creation form fields are empty', () => {
    cy.get('button[routerlink="create"]').click();
    cy.url().should('include', '/sessions/create');

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

  it('should successfully create a session and redirect to sessions', () => {
    cy.get('button[routerlink="create"]').click();
    cy.url().should('include', '/sessions/create');

    cy.intercept('POST', '/api/session', (req) => {
      expect(req.body).to.have.property('name', 'Test session');
      expect(req.body).to.have.property('date', '2024-04-05');
      expect(req.body).to.have.property('teacher_id').that.is.a('number');
      expect(req.body).to.have.property('description', 'This is a test description');
      req.reply({ statusCode: 200, body: { id: 1 } }); // Mock a successful response
    }).as('createSession');

    cy.get('input[formControlName="name"]').type('Test session');
    cy.get('input[formControlName="date"]').type('2024-04-05');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('.mat-option').first().click();
    cy.get('textarea[formControlName="description"]').type('This is a test description');
    cy.get('button[type="submit"]').click();

    cy.wait('@createSession').then((interception) => {
      expect(interception.response.statusCode).to.equal(200);
      expect(interception.response.body).to.have.property('id');
    });
    cy.get('.mat-snack-bar-container').should('be.visible').contains('Session created !');
    cy.url().should('include', '/sessions');
  });
});