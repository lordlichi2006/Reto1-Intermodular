# Base de datos de GozoShopApp

Ficheros para crear a mano la base de datos `reto1` de MongoDB: las colecciones con su validación, los índices y los datos iniciales.

La aplicación hace lo mismo sola la primera vez que arranca (`SchemaInitializer` y `FillData`), así que estos ficheros solo hacen falta para crear la base sin abrir la aplicación.

| Fichero | Qué hace |
|---|---|
| `01_collections.js` | Crea las colecciones `users`, `products`, `suppliers`, `discountPacks` y `shipments` con su `$jsonSchema` (`validationLevel: "strict"`, `validationAction: "error"`). Los esquemas son los mismos que `src/main/resources/schemas`. |
| `02_indexes.js` | Crea los índices: `users.email` único, `suppliers.cif` único y `shipments.customerId`. |
| `data/*.json` | Datos iniciales, los mismos que crea `FillData`: 10 usuarios, 5 proveedores, 10 productos, 3 packs de oferta y 4 envíos. Un documento por línea, en Extended JSON, como los exporta `mongoexport`. |

Todos los usuarios tienen la contraseña `1234`.

## Cómo cargarla

La base `reto1` tiene que estar vacía. Si ya existe, bórrala antes en Compass.

### Con MongoDB Compass

1. Conéctate a `mongodb://localhost:27017`.
2. Abre la consola de mongosh de Compass (abajo, **>_MONGOSH**), pega el contenido de `01_collections.js` y pulsa Enter. Haz lo mismo con `02_indexes.js`.
3. En cada colección de `reto1`, pulsa **Add Data → Import JSON or CSV file**, elige su fichero de `data/` (por ejemplo `data/users.json` para `users`) y pulsa **Import**.

### Con mongosh y mongoimport

Desde esta carpeta:

```
mongosh "mongodb://localhost:27017" 01_collections.js
mongosh "mongodb://localhost:27017" 02_indexes.js
mongoimport --uri "mongodb://localhost:27017" --db reto1 --collection users --file data/users.json
mongoimport --uri "mongodb://localhost:27017" --db reto1 --collection suppliers --file data/suppliers.json
mongoimport --uri "mongodb://localhost:27017" --db reto1 --collection products --file data/products.json
mongoimport --uri "mongodb://localhost:27017" --db reto1 --collection discountPacks --file data/discountPacks.json
mongoimport --uri "mongodb://localhost:27017" --db reto1 --collection shipments --file data/shipments.json
```

## Comprobarlo

- En Compass, la base `reto1` tiene que tener las cinco colecciones con datos.
- En cada colección, la pestaña **Validation** enseña su esquema y la pestaña **Indexes**, sus índices.
- Si insertas un documento que no cumple el esquema (por ejemplo, un producto sin `price`), MongoDB lo rechaza con «Document failed validation».

Los precios y los totales van como `{"$numberDouble": "..."}` para que MongoDB los guarde como `double` y no como entero, porque la aplicación los lee con `getDouble`.
