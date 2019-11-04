const MongoClient = require('mongodb').MongoClient;
const ObjectId = require('mongodb').ObjectID;
//const URL = 'mongodb://[username:password@]host:27017/[database]';
const URL = 'mongodb://localhost:27017/';

//You have to update below details to run the script
const DB = 'learn_db';
const COLLECTION = "learn_collection"

/**  Below are the utility functions to perform various operations on db and play with data  **/
async function main() {
    try {
        let startTime = new Date();
        console.log(`Start Time: ${startTime}`);
        let connection = await connectionPromise(URL);
        let db = selectDb(connection, DB);
        await solve(db);
        closeConnection(connection);
        let endTime = new Date();
        console.log(`End Time: ${endTime}`);
        console.log(`Time elapsed ${(endTime-startTime)/1000} seconds`);
    } catch (er) {
        console.log(er);
	process.exit(0);
    }
}

function connectionPromise(url) {
    return new Promise((resolve, reject) => {
        MongoClient.connect(url, {
            useNewUrlParser: true
        }, function (err, connection) {
            if (err) return reject(err);
            return resolve(connection);
        });
    });
}

function selectDb(connection, database) {
    return connection.db(database);
}

function findPromise(db, collection, query) {
    return new Promise((resolve, reject) => {
        db.collection(collection).find(query).toArray((err, records) => {
            if (err) return reject(err);
            return resolve(records);
        });
    });
}

function findOnePromise(db, collection, query) {
    return new Promise((resolve, reject) => {
        db.collection(collection).findOne(query, (err, record) => {
            if (err) return reject(err);
            return resolve(record);
        });
    });
}

function removePromise(db, collection, query) {
    return new Promise((resolve, reject) => {
        db.collection(collection).remove(query, (err, record) => {
            if (err) return reject(err);
            return resolve(record);
        });
    });
}

function insertManyPromise(db, collection, queries) {
    return new Promise((resolve, reject) => {
        db.collection(collection).insertMany(queries, (err, record) => {
            if (err) return reject(err);
            return resolve(record);
        });
    });
}


function doesCollectionExistPromise(db, collectionName) {
    return getCollectionPromise(db, collectionName)
        .then(collection => {
            if (collection != null)
                return true;
            return false;
        })
        .catch(er => false);
}

function createCollectionPromise(db, collectionName, options = {}) {
    return new Promise((resolve, reject) => {
        db.createCollection(collectionName, options, (err, records) => {
            if (err) return reject(err);
            return resolve(records);
        });
    });
}

function getCollectionsPromise(db) {
    return new Promise((resolve, reject) => {
        db.listCollections().toArray((err, records) => {
            if (err) return reject(err);
            return resolve(records);
        });
    });
}

function getCollectionPromise(db, collectionName) {
    return getCollectionsPromise(db).then(collections => {
        for (let i = 0; i != collections.length; ++i) {
            let collection = collections[i];
            if (collection.name == collectionName)
                return collection;
        }
        return null;
    });

}

function closeConnection(connection) {
    connection.close();
}

//write your stuff in this function
async function solve(db){
    let existingCollections = await getCollectionsPromise(db);
    console.log(`${existingCollections.length} collections found.`);

    if(existingCollections.length == 0){
        await createCollectionPromise(db, COLLECTION, {});
    }
    let collectionExist = await doesCollectionExistPromise(db, COLLECTION);
    console.log(`Does collection ${COLLECTION} exist? ${collectionExist}`);
    existingCollections = await getCollectionsPromise(db);
    console.log(`${existingCollections.length} collections found.`);
}

main();
