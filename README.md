**API v1 Documentation**

The **AuthorizationController** is responsible for handling user authentication operations.

1. Endpoint: POST /api/v1/login
- Description: Authenticate and log in a user. If the user is already in the system, it is enough to enter just a personal code.
- Request Body: A LoginForm object containing user login credentials (e.g., personal code, username and password).
- Response: Returns a LoginStatus indicating the result of the login attempt. If the login is successful (status is SUCCESS), it returns an OK response with the login status. Otherwise, it returns a Bad Request status with the login status to indicate the reason for login failure.

The **EventController** is responsible for managing events and user invitations. It provides various endpoints to create, retrieve, update, and delete events and invitations.

1. Endpoint: POST /api/v1/event
- Description: Create a new event.
- Request Body: An EventForm object containing event details. Date cannot be past or present, only future.
- Request Header: personalCode - The personal code of the user creating the event.
- Response: Returns the ID of the created event if successful; otherwise, returns a Bad Request status with an error message.

2. Endpoint: GET /api/v1/event/{eventId}
- Description: Retrieve event details by ID.
- Path Parameter: eventId - The ID of the event to retrieve.
- Request Header: personalCode - The personal code of the user requesting the event.
- Response: Returns the event details if the user has access; otherwise, returns Forbidden status with an error message.

3. Endpoint: POST /api/v1/event/{eventId}/user
- Description: Invite user to an event.
- Path Parameter: eventId - The ID of the event.
- Request Body: A UserInvitationForm object containing user invitation details.
- Request Header: personalCode - The personal code of the user adding the user to the event.
- Response: Returns the created user invitation details if successful; otherwise, returns a Bad Request status with an error message.

4. Endpoint: PUT /api/v1/event/{eventId}/user/{invitationId}

- Description: Update user invitation data for an event.
- Path Parameters: eventId - The ID of the event, invitationId - The ID of the user invitation.
- Request Body: A UserInvitationForm object containing updated user invitation details.
- Request Header: personalCode - The personal code of the user making the update.
- Response: Returns the updated user invitation details if successful; otherwise, returns a Bad Request status with an error message.

4. Endpoint: POST /api/v1/event/{eventId}/company
- Description: Add a company to an event.
- Path Parameter: eventId - The ID of the event.
- Request Body: A CompanyInvitationForm object containing company invitation details.
- Request Header: personalCode - The personal code of the user adding the company to the event.
- Response: Returns the created company invitation details if successful; otherwise, returns a Bad Request status with an error message.

5. Endpoint: PUT /api/v1/event/{eventId}/company/{invitationId}
- Description: Update company invitation data for an event.
- Path Parameters: eventId - The ID of the event, invitationId - The ID of the company invitation.
- Request Body: A CompanyInvitationForm object containing updated company invitation details.
- Request Header: personalCode - The personal code of the user making the update.
- Response: Returns the updated company invitation details if successful; otherwise, returns a Bad Request status with an error message.

6. Endpoint: DELETE /api/v1/event/{eventId}/user/{invitationId}
- Description: Remove a user invitation from an event.
- Path Parameters: eventId - The ID of the event, invitationId - The ID of the user invitation to remove.
- Request Header: personalCode - The personal code of the user removing the user invitation.
- Response: Returns a success message if the removal is successful; otherwise, returns a Bad Request status with an error message.

7. Endpoint: DELETE /api/v1/event/{eventId}/company/{invitationId}
- Description: Remove a company invitation from an event.
- Path Parameters: eventId - The ID of the event, invitationId - The ID of the company invitation to remove.
- Request Header: personalCode - The personal code of the user removing the company invitation.
- Response: Returns a success message if the removal is successful; otherwise, returns a Bad Request status with an error message.

8. Endpoint: DELETE /api/v1/event/{eventId}/user
- Description: Allow a user to leave an event.
- Path Parameter: eventId - The ID of the event.
- Request Header: personalCode - The personal code of the user leaving the event.
- Response: Returns a success message if the user successfully leaves the event; otherwise, returns a Bad Request status with an error message.

9. Endpoint: POST /api/v1/event/{eventId}/moderator
- Description: Сhange the moderator status of a user at an event.
- Path Parameter: eventId - The ID of the event.
- Request Body: A User object representing the user to be set as a moderator.
- Request Header: personalCode - The personal code of the user making the change.
- Response: Returns the user with updated moderator status if successful; otherwise, returns a Bad Request status with an error message.

The **UserController** is responsible for managing user-specific operations related to events.

1. Endpoint: GET /api/v1/user
- Description: Retrieve a list of all events associated with a user.
- Request Header: personalCode - The personal code of the user for whom to retrieve events.
- Response: Returns a list of events associated with the user if successful; otherwise, returns an appropriate status code with an error message.
- This documentation covers the endpoint provided by the UserController for retrieving events associated with a user. Ensure that you provide the necessary request headers when making requests to this endpoint.
