db = db.getSiblingDB("reto1");

db.createCollection("users", {
    validator: {
        $jsonSchema: {
          "bsonType": "object",
          "title": "users: customers and workers (field userType)",
          "required": ["userType", "name", "email", "password", "idDocument"],
          "properties": {
            "userType": { "enum": ["customer", "worker"] },
            "name": { "bsonType": "string", "minLength": 1 },
            "email": { "bsonType": "string", "pattern": "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$" },
            "password": { "bsonType": "string", "minLength": 1 },
            "idDocument": { "bsonType": "string", "minLength": 1 },
            "role": { "enum": ["WORKER", "ADMIN"] },
            "type": { "enum": ["INDIVIDUAL", "BUSINESS"] },
            "totalSpent": { "bsonType": "number", "minimum": 0 },
            "addresses": { "bsonType": "array", "items": { "bsonType": "string" } },
            "cart": {
              "bsonType": "array",
              "items": {
                "bsonType": "object",
                "required": ["productId", "name", "price", "units"],
                "properties": {
                  "productId": { "bsonType": "objectId" },
                  "name": { "bsonType": "string" },
                  "brand": { "bsonType": "string" },
                  "price": { "bsonType": "number", "minimum": 0 },
                  "units": { "bsonType": "int", "minimum": 1 }
                }
              }
            },
            "shipmentIds": { "bsonType": "array", "items": { "bsonType": "objectId" } }
          },
          "anyOf": [
            { "properties": { "userType": { "enum": ["worker"] } }, "required": ["role"] },
            { "properties": { "userType": { "enum": ["customer"] } }, "required": ["type"] }
          ]
        }
    },
    validationLevel: "strict",
    validationAction: "error"
});
print("Collection created: users");

db.createCollection("products", {
    validator: {
        $jsonSchema: {
          "bsonType": "object",
          "title": "products",
          "required": ["name", "brand", "price", "stock"],
          "properties": {
            "name": { "bsonType": "string", "minLength": 1 },
            "brand": { "bsonType": "string", "minLength": 1 },
            "price": { "bsonType": "number", "minimum": 0 },
            "stock": { "bsonType": "int", "minimum": 0 },
            "description": { "bsonType": "string" },
            "suppliers": {
              "bsonType": "array",
              "items": {
                "bsonType": "object",
                "required": ["supplierId", "name", "cif"],
                "properties": {
                  "supplierId": { "bsonType": "objectId" },
                  "name": { "bsonType": "string" },
                  "cif": { "bsonType": "string" }
                }
              }
            }
          }
        }
    },
    validationLevel: "strict",
    validationAction: "error"
});
print("Collection created: products");

db.createCollection("suppliers", {
    validator: {
        $jsonSchema: {
          "bsonType": "object",
          "title": "suppliers",
          "required": ["name", "cif", "email"],
          "properties": {
            "name": { "bsonType": "string", "minLength": 1 },
            "cif": { "bsonType": "string", "pattern": "^[A-Z][0-9]{7}[0-9A-Z]$" },
            "email": { "bsonType": "string", "pattern": "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$" },
            "telephone": { "bsonType": "string", "pattern": "^[0-9+ ]{9,15}$" }
          }
        }
    },
    validationLevel: "strict",
    validationAction: "error"
});
print("Collection created: suppliers");

db.createCollection("discountPacks", {
    validator: {
        $jsonSchema: {
          "bsonType": "object",
          "title": "discountPacks",
          "required": ["name", "discountPercentage", "productIds", "remainingUnits"],
          "properties": {
            "name": { "bsonType": "string", "minLength": 1 },
            "discountPercentage": { "bsonType": "number", "minimum": 0, "maximum": 100 },
            "productIds": { "bsonType": "array", "minItems": 1, "items": { "bsonType": "objectId" } },
            "remainingUnits": { "bsonType": "int", "minimum": 0 }
          }
        }
    },
    validationLevel: "strict",
    validationAction: "error"
});
print("Collection created: discountPacks");

db.createCollection("shipments", {
    validator: {
        $jsonSchema: {
          "bsonType": "object",
          "title": "shipments",
          "required": ["customerId", "createdAt", "address", "lines", "total", "status"],
          "properties": {
            "customerId": { "bsonType": "objectId" },
            "createdAt": { "bsonType": "date" },
            "address": { "bsonType": "string", "minLength": 1 },
            "lines": {
              "bsonType": "array",
              "minItems": 1,
              "items": {
                "bsonType": "object",
                "required": ["productId", "name", "price", "units"],
                "properties": {
                  "productId": { "bsonType": "objectId" },
                  "name": { "bsonType": "string" },
                  "price": { "bsonType": "number", "minimum": 0 },
                  "units": { "bsonType": "int", "minimum": 1 }
                }
              }
            },
            "total": { "bsonType": "number", "minimum": 0 },
            "trackingId": { "bsonType": "string" },
            "shipmentCompany": { "bsonType": "string" },
            "status": { "enum": ["IN_PROGRESS", "IN_SHIPMENT", "ARRIVED", "CANCELLED"] }
          }
        }
    },
    validationLevel: "strict",
    validationAction: "error"
});
print("Collection created: shipments");

print("Collections in reto1: " + db.getCollectionNames().join(", "));
