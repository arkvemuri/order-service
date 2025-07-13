// MongoDB initialization script
db = db.getSiblingDB('orderdb');

// Create a user for the orderdb database
db.createUser({
  user: 'orderuser',
  pwd: 'orderpass',
  roles: [
    {
      role: 'readWrite',
      db: 'orderdb'
    }
  ]
});

// Create collections if needed
db.createCollection('orders');
db.createCollection('sequence');

// Insert initial sequence document for auto-increment
db.sequence.insertOne({
  _id: 'order_sequence',
  seq: 0
});

print('Database orderdb initialized successfully!');