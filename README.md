# EmporiumZFB

Link repositorio: "https://github.com/KevinCeballos700/EmporiumZFB.git"

Implementé en NetBeans un flujo completo de backend y base de datos para un pequeño comercio: añadí a la base de datos la columna identification_unc (identificador único de 7 dígitos) en users, creé las tablas products, orders y order_items, y desarrollé en Java los modelos, DAOs y servlets necesarios para registrar usuarios (generando identification_unc y hasheando contraseñas), crear productos (asignando automáticamente un sku formateado tras el INSERT), y crear órdenes (aceptando identification_unc en el JSON, resolviendo al user_id, validando y almacenando la orden y sus items en una transacción); además depuré y corregí fallos (resolución de identification_unc, manejo de FK de productos, compilación), añadí ejemplos para Postman y una función frontend mínima para enviar órdenes, todo para garantizar integridad referencial, trazabilidad de usuarios y un flujo de pruebas reproducible. Ademas de eso, use TomCat, phP myAdmin y Postman para pruebas.

Aclaro esto fue una prueba solo para postman, en la pagina web aun no se puede hacer esto por falta de recursos...
