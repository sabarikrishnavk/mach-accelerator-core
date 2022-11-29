
const params = require('../es-mapping/inventory');
const PRODUCTS = require('../data/inventory.json').inventory;
const ElasticSearch = require('elasticsearch');
const _ = require('lodash');
const { zip } = require('lodash');

/**
 * *** ElasticSearch *** client
 * @type {Client}
 */
const client = new ElasticSearch.Client({
  hosts: ['http://127.0.0.1:9200']
});

const INDEXNAME = "inventory";

async function deleteIndex (indexName){
  console.log('deleteIndex');

  return await  new Promise(function (resolve, reject) {
    client.indices.delete(
    {
      index: indexName
    },
    (error, response, status) => {
      if(!error) {
        console.info("🚀 Deleted index");
        createIndex(indexName);
        console.info(response);
      } else {
        console.info(error);
      }
  
    }
    );
  });
}   

async function createIndex(indexName){
  console.log('createIndex');

  return await  new Promise(function (resolve, reject) {
    client.indices.create(
      {
        index: indexName,
        body: params
      },
      (error, response, status) => {
        if(!error) {
          console.info("\n🚀 Created a new index");
          loadIndex(collectionBulk);
          console.info(response);
          console.info('\n');
        } else {
          console.info(error);
        }

      }
    ); 
  });
}   

async function loadIndex (collectionBulk){
  console.log('loadIndex');

  return await  new Promise(function (resolve, reject) {

    
    client.bulk({body: collectionBulk}, function (err, r) {
      if (err) {
        console.log(`Failed Bulk operation\n`, err);
      } else {
        console.log(`🚀 Successfully imported ${_.keys(PRODUCTS).length} items \n`);
      }
    }); 
  });

} 
let initialBulk = {index: {_index: INDEXNAME}};
let collectionBulk = [];
_.map(_.keys(PRODUCTS), id => {
  collectionBulk = [
    ...collectionBulk, 
    initialBulk, 
    PRODUCTS[id]
  ];
});
 
async function init(){


 
  let isExist = await client.indices.exists({
    index: INDEXNAME 
  }); 

  if(isExist){
    console.log('index exists'); 
    deleteIndex(INDEXNAME);  
  }  else{ 
    createIndex(INDEXNAME);  
  }


}
init(); 

