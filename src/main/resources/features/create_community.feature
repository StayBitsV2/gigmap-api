Feature: Crear y gestionar comunidades
  Como fan de la música
  Quiero crear y gestionar comunidades
  Para compartir contenido y conectar con otros usuarios

  Background:
    Given existe un usuario registrado en el sistema

  Scenario: Crear comunidad exitosamente
    When el usuario crea una comunidad con:
      | name        | Rock Fans Perú                          |
      | description | Comunidad para fans del rock en Perú    |
      | imageUrl    | https://image.com/community-rock.png    |
    Then el sistema debe crear la comunidad correctamente
    And la comunidad debe tener nombre "Rock Fans Perú"
    And la comunidad debe tener descripción "Comunidad para fans del rock en Perú"
    And la comunidad debe tener una imagen "https://image.com/community-rock.png"

  Scenario: Intentar crear comunidad con datos inválidos
    When el usuario intenta crear una comunidad con:
      | name        |                                         |
      | description |                                        |
      | imageUrl    | not-a-valid-url                        |
    Then el sistema debe rechazar la creación de la comunidad
    And debe mostrarse un mensaje indicando datos inválidos

  Scenario: Unirse a una comunidad existente
    Given existe una comunidad llamada "Rock Fans Perú"
    And el usuario aún no es miembro de esta comunidad
    When el usuario se une a la comunidad
    Then el usuario debe aparecer en la lista de miembros de la comunidad

  Scenario: Abandonar una comunidad
    Given existe una comunidad llamada "Rock Fans Perú"
    And el usuario ya es miembro de esta comunidad
    When el usuario abandona la comunidad
    Then el usuario ya no debe aparecer en la lista de miembros de la comunidad