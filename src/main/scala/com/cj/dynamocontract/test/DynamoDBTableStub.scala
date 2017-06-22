package com.cj.dynamocontract.test

import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.spec._
import com.amazonaws.services.dynamodbv2.model.{DeleteTableResult, TableDescription}
import com.cj.dynamocontract.DynamoDBTableContract

trait DynamoDBTableStub extends DynamoDBTableContract {
  def delete(): DeleteTableResult = ???
  def deleteItem(spec: DeleteItemSpec): DeleteItemOutcome = ???
  def describe(): TableDescription = ???
  def getDescription: TableDescription = ???
  def getIndex(indexName: String): Index = ???
  def getItem(spec: GetItemSpec): Item = ???
  def getItemOutcome(spec: GetItemSpec): GetItemOutcome = ???
  def getTableName: String = ???
  def putItem(item: Item): PutItemOutcome = ???
  def query(spec: QuerySpec): ItemCollection[QueryOutcome] = ???
  def scan(spec: ScanSpec): ItemCollection[ScanOutcome] = ???
  def updateItem(spec: UpdateItemSpec): UpdateItemOutcome = ???
  def updateTable(spec: UpdateTableSpec): TableDescription = ???
  def waitForActive(): TableDescription = ???
  def waitForActiveOrDelete(): TableDescription = ???
  def waitForAllActiveOrDelete(): TableDescription = ???
  def waitForDelete(): Unit = ???
}
