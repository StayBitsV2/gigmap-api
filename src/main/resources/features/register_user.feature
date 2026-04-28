Feature: Registro de usuario
  Como visitante de la plataforma
  Quiero crear una cuenta nueva
  Para poder iniciar sesión y acceder a las funcionalidades de la aplicación

  Scenario: Registro exitoso
    When el visitante envía los datos de registro:
      | email    | newuser@example.com |
      | username | newuser             |
      | password | password123         |
      | role     | USER                |
    Then el sistema debe crear el usuario correctamente
    And debe devolver un token JWT válido
    And debe responder con el mensaje "User registered successfully"

  Scenario: Registro fallido por email duplicado
    Given ya existe un usuario con email "existing@example.com"
    When el visitante intenta registrarse con:
      | email    | existing@example.com |
      | username | newusername          |
      | password | password123          |
      | role     | USER                 |
    Then el sistema debe rechazar el registro
    And debe responder con el error "Email already registered"

  Scenario: Registro fallido por username duplicado
    Given ya existe un usuario con username "takenuser"
    When el visitante intenta registrarse con:
      | email    | newperson@example.com |
      | username | takenuser             |
      | password | password123           |
      | role     | USER                  |
    Then el sistema debe rechazar el registro
    And debe responder con un mensaje indicando que el username ya está en uso

  Scenario: Registro fallido por datos inválidos
    When el visitante envía datos inválidos para registrarse:
      | email    | not-an-email |
      | username | us           |
      | password | 123          |
      | role     |              |
    Then el sistema debe rechazar el registro
    And debe mostrar un mensaje indicando datos inválidos