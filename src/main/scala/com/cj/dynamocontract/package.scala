package com.cj


import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.spec._
import com.amazonaws.services.dynamodbv2.model.{DeleteTableResult, TableDescription}

package object dynamocontract {
  trait DynamoDBTableContract{
    def delete():DeleteTableResult
    def deleteItem(spec: DeleteItemSpec):DeleteItemOutcome
    def describe(): TableDescription
    def getDescription: TableDescription
    def getIndex(indexName: String): Index
    def getItem(spec: GetItemSpec): Item
    def getItemOutcome(spec: GetItemSpec): GetItemOutcome
    def getTableName: String
    def putItem(item: Item): PutItemOutcome
    def query(spec: QuerySpec):  ItemCollection[QueryOutcome]
    def scan(spec: ScanSpec):  ItemCollection[ScanOutcome]
    def toString:String
    def updateItem(spec: UpdateItemSpec): UpdateItemOutcome
    def updateTable(spec: UpdateTableSpec): TableDescription
    def waitForActive(): TableDescription
    def waitForActiveOrDelete(): TableDescription
    def waitForAllActiveOrDelete(): TableDescription
    def waitForDelete(): Unit
  }

  def dynamoTable(table:Table):DynamoDBTableContract = {
    new  DynamoDBTableContract(){
      override def delete(): DeleteTableResult                          = table.delete
      override def deleteItem(spec: DeleteItemSpec): DeleteItemOutcome  = table.deleteItem(spec)
      override def describe(): TableDescription                         = table.describe
      override def getDescription: TableDescription                     = table.getDescription
      override def getIndex(indexName: String): Index                   = table.getIndex(indexName)
      override def getItem(spec: GetItemSpec): Item                     = table.getItem(spec)
      override def getItemOutcome(spec: GetItemSpec): GetItemOutcome    = table.getItemOutcome()
      override def getTableName: String                                 = table.getTableName
      override def putItem(item: Item): PutItemOutcome                  = table.putItem(item)
      override def query(spec: QuerySpec): ItemCollection[QueryOutcome] = table.query(spec)
      override def scan(spec: ScanSpec): ItemCollection[ScanOutcome]    = table.scan(spec)
      override def toString: String                                     = table.toString
      override def updateItem(spec: UpdateItemSpec): UpdateItemOutcome  = table.updateItem(spec)
      override def updateTable(spec: UpdateTableSpec): TableDescription = table.updateTable(spec)
      override def waitForActive(): TableDescription                    = table.waitForActive
      override def waitForActiveOrDelete(): TableDescription            = table.waitForActiveOrDelete
      override def waitForAllActiveOrDelete(): TableDescription         = table.waitForAllActiveOrDelete
      override def waitForDelete(): Unit                                = table.waitForDelete()
    }
  }

  def getDynamoTable(tableName: String): DynamoDBTableContract = dynamoTable(new DynamoDB(Regions.US_WEST_1).getTable(tableName))

}