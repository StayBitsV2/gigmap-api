Feature: Inicio de sesión de usuario
  Como usuario registrado
  Quiero iniciar sesión con mis credenciales
  Para poder acceder a mi cuenta y obtener un token válido

  Background:
    Given existe un usuario registrado con email "test@example.com" y contraseña "password123"

  Scenario: Login exitoso con email
    When el usuario envía las credenciales:
      | emailOrUsername | test@example.com |
      | password        | password123      |
    Then el sistema debe autenticar al usuario correctamente
    And debe devolver un token JWT válido
    And debe responder con el mensaje "Login successful"

  Scenario: Login exitoso con username
    When el usuario envía las credenciales:
      | emailOrUsername | testuser         |
      | password        | password123      |
    Then el sistema debe autenticar al usuario correctamente
    And debe devolver un token JWT válido
    And debe responder con el mensaje "Login successful"

  Scenario: Login fallido con contraseña incorrecta
    When el usuario envía las credenciales:
      | emailOrUsername | test@example.com |
      | password        | wrongPassword    |
    Then el sistema debe rechazar el inicio de sesión
    And debe responder con el error "Invalid credentials"

  Scenario: Login fallido con usuario inexistente
    When el usuario envía las credenciales:
      | emailOrUsername | unknown@example.com |
      | password        | password123         |
    Then el sistema debe rechazar el inicio de sesión
    And debe responder con el error "Invalid credentials"