package com.cj.dynamocontract.test

import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.spec._
import com.amazonaws.services.dynamodbv2.model.{DeleteTableResult, TableDescription}
import com.cj.dynamocontract.DynamoDBTableContract

object DynamoDBTableStub extends DynamoDBTableContract {
  override def delete: () => DeleteTableResult = ???
  override def deleteItem: DeleteItemSpec => DeleteItemOutcome = ???
  override def describe: () => TableDescription = ???
  override def getDescription: () => TableDescription = ???
  override def getIndex: String => Index = ???
  override def getItem: GetItemSpec => Item = ???
  override def getItemOutcome: GetItemSpec => GetItemOutcome = ???
  override def getTableName: () => String = ???
  override def putItem: Item => PutItemOutcome = ???
  override def query: QuerySpec => ItemCollection[QueryOutcome] = ???
  override def scan: ScanSpec => ItemCollection[ScanOutcome] = ???
  override def updateItem: UpdateItemSpec => UpdateItemOutcome = ???
  override def updateTable: UpdateTableSpec => TableDescription = ???
  override def waitForActive: () => TableDescription = ???
  override def waitForActiveOrDelete: () => TableDescription = ???
  override def waitForAllActiveOrDelete: () => TableDescription = ???
  override def waitForDelete: () => Unit = ???
}
