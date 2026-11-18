db = db.getSiblingDB("reto1");

db.users.createIndex({ email: 1 }, { unique: true });
db.suppliers.createIndex({ cif: 1 }, { unique: true });
db.shipments.createIndex({ customerId: 1 });

print("Indexes of users:");
printjson(db.users.getIndexes());
print("Indexes of suppliers:");
printjson(db.suppliers.getIndexes());
print("Indexes of shipments:");
printjson(db.shipments.getIndexes());
