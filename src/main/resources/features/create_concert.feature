Feature: Crear concierto
  Como artista
  Quiero registrar un concierto en la plataforma
  Para que los usuarios puedan visualizarlo y asistir

  Background:
    Given existe un artista con rol "ARTIST"
    And existe un venue válido llamado "Estadio San Marcos"
    And existe una plataforma válida llamada "Ticketmaster"

  Scenario: Crear concierto exitosamente
    When el artista crea un concierto con:
      | title         | Concierto Coldplay                     |
      | description   | Gira mundial                           |
      | date          | 2025-11-29T20:00:00                    |
      | imageUrl      | https://image.com/concert.jpg          |
      | status        | PUBLICADO                              |
      | genre         | ROCK                                   |
    Then el sistema debe crear el concierto correctamente
    And el concierto debe tener título "Concierto Coldplay"
    And el concierto debe tener género "ROCK"
    And el concierto debe tener estado "PUBLICADO"

  Scenario: Intentar crear concierto con usuario no artista
    Given existe un usuario con rol "CLIENT"
    When intenta crear un concierto
    Then el sistema debe rechazar la operación
    And se debe indicar que el usuario no es un artista válido

  Scenario: Intentar crear concierto con datos inválidos
    When el artista intenta crear un concierto con campos vacíos
    Then el sistema debe rechazar el concierto
    And debe mostrar un mensaje indicando datos inválidos