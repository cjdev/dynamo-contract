package com.cj


import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.spec._
import com.amazonaws.services.dynamodbv2.model.{DeleteTableResult, TableDescription}

package object dynamocontract {
  trait DynamoDBContract{
    def delete:()=>DeleteTableResult
    def deleteItem:DeleteItemSpec=>DeleteItemOutcome
    def describe:()=>TableDescription
    def getDescription:()=>TableDescription
    def getIndex:String=>Index
    def getItem:GetItemSpec=>Item
    def getItemOutcome:GetItemSpec=>GetItemOutcome
    def getTableName:()=>String
    def putItem:Item=>PutItemOutcome
    def query:QuerySpec=> ItemCollection[QueryOutcome]
    def scan:ScanSpec=> ItemCollection[ScanOutcome]
    def toString:String
    def updateItem:UpdateItemSpec=>UpdateItemOutcome
    def updateTable:UpdateTableSpec=>TableDescription
    def waitForActive:()=>TableDescription
    def waitForActiveOrDelete:()=>TableDescription
    def waitForAllActiveOrDelete:()=>TableDescription
    def waitForDelete:()=>Unit
  }

  def dynamoTable(table:Table):DynamoDBContract = {
    new  DynamoDBContract(){
      override def delete                   = table.delete
      override def deleteItem               = table.deleteItem
      override def describe                 = table.describe
      override def getDescription           = table.getDescription
      override def getIndex                 = table.getIndex
      override def getItem                  = table.getItem
      override def getItemOutcome           = table.getItemOutcome
      override def getTableName             = table.getTableName
      override def putItem                  = table.putItem
      override def query                    = table.query
      override def scan                     = table.scan
      override def toString                 = table.toString
      override def updateItem               = table.updateItem
      override def updateTable              = table.updateTable
      override def waitForActive            = table.waitForActive
      override def waitForActiveOrDelete    = table.waitForActiveOrDelete
      override def waitForAllActiveOrDelete = table.waitForAllActiveOrDelete
      override def waitForDelete            = table.waitForDelete
    }
  }

  def getDynamoTable(tableName: String): DynamoDBContract = dynamoTable(new DynamoDB(Regions.US_WEST_1).getTable(tableName))

}