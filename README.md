# Ecommerce Spring boot webflux
Aplicación de gestión de pedidos para un sistema de comercio electrónico. La
aplicación debe permitir crear, leer, actualizar y eliminar pedidos, así como listar todos los
pedidos y obtener detalles de un pedido específico. Un pedido tiene un cliente, una lista de
productos y un total. Los productos y clientes deben almacenarse en bases de datos
relacionales, mientras que los pedidos deben almacenarse en una base de datos no
relacional.

- Para desplegar los web services se desplegaron en Render con ayuda de un DockerFile.
- El PostgreSql está desplegado en Render.
- El MongoDB está desplegado en la plataforma Atlas.


## Requisitos

- Spring boot 3 webflux
- PostgreSql
- MongoDB
- Despliegue en Render

## Pruebas Postman (Pedido)
- Crear pedido (POST)

    url: https://ecommerce-aywx.onrender.com/pedido 

    body:
    ```
    {
        "clienteId": 1,
        "productos": [
            { "productoId": 1, "cantidad": 2 },
            { "productoId": 3, "cantidad": 2 }
        ],
        "total": 150.00
    }
    ```
    Listar pedido (GET)

    url: https://ecommerce-aywx.onrender.com/pedido 

- Listar pedido por ID (GET)

    url: https://ecommerce-aywx.onrender.com/pedido/671eff861f01922a50c15495

- Actualizar pedido por ID (PUT)

    url: https://ecommerce-aywx.onrender.com/pedido/671eff861f01922a50c15495
    body:
    ```
    {
        "clienteId": 1,
        "productos": [
            { "productoId": 1, "cantidad": 2 },
            { "productoId": 3, "cantidad": 2 }
        ],
        "total": 150.00
    }

- Eliminar pedido por ID (DELETE)

    url: https://ecommerce-aywx.onrender.com/pedido/671eff861f01922a50c15495


Descargar el postman para interactuar con lso otros enpoints (Producto, Cliente)