
@smoke @cruce
Feature: Cruce de Facturación en ORBIT
  Como usuario de ORBIT
  Quiero ejecutar el cruce de facturación
  Para visualizar los resultados correctamente

  Background:
    Given que el usuario inicia sesión en ORBIT con usuario "gc__@grupocinte.com" y clave "Estreno32."

  Scenario: Ejecutar cruce con filtros
    And que el usuario abre la pantalla de cruce de facturación
    When aplica filtros de fecha "2025-01-01" a "2025-01-31" con estado "ACTIVO"
    And ejecuta el cruce con los parámetros por defecto
    Then debería visualizar resultados en la pantalla
