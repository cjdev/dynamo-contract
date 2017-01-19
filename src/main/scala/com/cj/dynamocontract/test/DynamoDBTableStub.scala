package com.cj.dynamocontract.test

import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.spec._
import com.amazonaws.services.dynamodbv2.model.{DeleteTableResult, TableDescription}
import com.cj.dynamocontract.DynamoDBTableContract

class DynamoDBTableStub extends DynamoDBTableContract {
  override def delete(): DeleteTableResult = ???
  override def deleteItem(spec: DeleteItemSpec): DeleteItemOutcome = ???
  override def describe(): TableDescription = ???
  override def getDescription: TableDescription = ???
  override def getIndex(indexName: String): Index = ???
  override def getItem(spec: GetItemSpec): Item = ???
  override def getItemOutcome(spec: GetItemSpec): GetItemOutcome = ???
  override def getTableName: String = ???
  override def putItem(item: Item): PutItemOutcome = ???
  override def query(spec: QuerySpec): ItemCollection[QueryOutcome] = ???
  override def scan(spec: ScanSpec): ItemCollection[ScanOutcome] = ???
  override def updateItem(spec: UpdateItemSpec): UpdateItemOutcome = ???
  override def updateTable(spec: UpdateTableSpec): TableDescription = ???
  override def waitForActive(): TableDescription = ???
  override def waitForActiveOrDelete(): TableDescription = ???
  override def waitForAllActiveOrDelete(): TableDescription = ???
  override def waitForDelete(): Unit = ???
}
