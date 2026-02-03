# language: es
Característica: Gestión Integral de Conciliación de Facturas
  Yo como analista de facturación
  Quiero realizar cruces de facturas validando reglas de negocio
  Para mantener los saldos actualizados y auditados

  Antecedentes:
    Dado que el analista inicia sesion en Orbit con sus credenciales corporativas
    Y navega a traves del menu principal hacia el modulo de "Cruce/Descruce de Facturacion"

  @filtros @regresion
  Escenario: Filtrar facturas por cliente especifico
    Dado que selecciona el cliente "seguros alfa" en el filtro de busqueda
    Cuando ejecuta la consulta de facturas
    Entonces la tabla de resultados deberia mostrar unicamente registros asociados al cliente "seguros alfa"

  @cruce @critico
  Esquema del escenario: Realizar cruce de factura con asignacion de monto parcial
    Dado que selecciona la opcion de "Realizar Cruce" para la factura numero "<id_factura>"
    Cuando asocia al consultor "<consultor>" a la factura moviendolo a la lista de pendientes
    Y asigna el valor a cruzar de "<monto>" modificando el saldo sugerido
    Y finaliza el proceso de cruce registrando la observacion "<comentario>"
    Entonces deberia visualizar el mensaje de exito "¡Cruce registrado en el sistema!"

    Ejemplos:
      | id_factura | consultor                 | monto | comentario          |
      | 1088       | Jenny Raquel Gómez Mora   | 500   | Prueba Automatizada |

  @cruce @reglas_negocio
  Esquema del escenario: Validar bloqueo al intentar cruzar montos invalidos
    Dado que selecciona la opcion de "Realizar Cruce" para la factura numero "<id_factura>"
    Cuando asocia al consultor "<consultor>" a la factura moviendolo a la lista de pendientes
    Y intenta asignar el valor invalido de "<monto_invalido>" al consultor
    Entonces deberia visualizar el mensaje de error "<mensaje_esperado>"

    Ejemplos:
      | id_factura | consultor                 | monto_invalido | mensaje_esperado         |
      | 1088       | Jenny Raquel Gómez Mora   | 0              | Cruce sin valor asignado |