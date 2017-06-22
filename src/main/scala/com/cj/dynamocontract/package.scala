package com.cj

import com.amazonaws.{AmazonClientException, AmazonWebServiceRequest}
import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.spec._
import com.amazonaws.services.dynamodbv2.model._

package object dynamocontract {

  @deprecated("Replace with DynamoDBTableContract.fromSDKTable")
  def dynamoTable(table: Table): DynamoDBTableContract =
    DynamoDBTableContract.fromSDKTable(table)

  @deprecated("Replace with TableConfig.toTable")
  def getDynamoTable(tableName: String): DynamoDBTableContract =
    TableConfig(tableName).toTable

  @deprecated("Replace with TableConfig.toTable")
  def getDynamoTable(
                      accessKeyId: String,
                      secretKey: String,
                      tableName: String
                    ): DynamoDBTableContract =
    TableConfig(tableName).withCredentials(accessKeyId, secretKey).toTable

  trait DynamoDBTableContract {
    def delete(): DeleteTableResult
    def deleteItem(spec: DeleteItemSpec): DeleteItemOutcome
    def describe(): TableDescription
    def getDescription: TableDescription
    def getIndex(indexName: String): Index
    def getItem(spec: GetItemSpec): Item
    def getItemOutcome(spec: GetItemSpec): GetItemOutcome
    def getTableName: String
    def putItem(item: Item): PutItemOutcome
    def query(spec: QuerySpec): ItemCollection[QueryOutcome]
    def scan(spec: ScanSpec): ItemCollection[ScanOutcome]
    def updateItem(spec: UpdateItemSpec): UpdateItemOutcome
    def updateTable(spec: UpdateTableSpec): TableDescription
    def waitForActive(): TableDescription
    def waitForActiveOrDelete(): TableDescription
    def waitForAllActiveOrDelete(): TableDescription
    def waitForDelete(): Unit
    def toString: String
  }

  object DynamoDBTableContract {

    def fromSDKTable(table: Table): DynamoDBTableContract =
      new DynamoDBTableContract {
        def delete(): DeleteTableResult                          = table.delete
        def deleteItem(spec: DeleteItemSpec): DeleteItemOutcome  = table.deleteItem(spec)
        def describe(): TableDescription                         = table.describe
        def getDescription: TableDescription                     = table.getDescription
        def getIndex(indexName: String): Index                   = table.getIndex(indexName)
        def getItem(spec: GetItemSpec): Item                     = table.getItem(spec)
        def getItemOutcome(spec: GetItemSpec): GetItemOutcome    = table.getItemOutcome()
        def getTableName: String                                 = table.getTableName
        def putItem(item: Item): PutItemOutcome                  = table.putItem(item)
        def query(spec: QuerySpec): ItemCollection[QueryOutcome] = table.query(spec)
        def scan(spec: ScanSpec): ItemCollection[ScanOutcome]    = table.scan(spec)
        def updateItem(spec: UpdateItemSpec): UpdateItemOutcome  = table.updateItem(spec)
        def updateTable(spec: UpdateTableSpec): TableDescription = table.updateTable(spec)
        def waitForActive(): TableDescription                    = table.waitForActive
        def waitForActiveOrDelete(): TableDescription            = table.waitForActiveOrDelete
        def waitForAllActiveOrDelete(): TableDescription         = table.waitForAllActiveOrDelete
        def waitForDelete(): Unit                                = table.waitForDelete()
        override def toString: String                            = table.toString
      }

    def fromTableConfig(tableConfig: TableConfig): DynamoDBTableContract =
      tableConfig.toTable
  }

  trait ClientConfig {

    def retryDelay(
                    originalRequest: AmazonWebServiceRequest,
                    exception: AmazonClientException,
                    retriesAttempted: Int
                  ): Option[Long]

    def maxErrorRetry: Int
  }

  object ClientConfig {

    def exponentialBackoff(
                            baseDelay: Int = 500,
                            maxDelay: Long = 20000,
                            throttledDelay: Int = 25,
                            throttledMax: Long = 20000,
                            maxRetry: Int = 10
                          ): ClientConfig = {

      def expMax(b: Long, n: Int, m: Long): Long =
        math.min((1 to n).map(_ => b).product, m)

      new ClientConfig {

        def retryDelay(
                        originalRequest: AmazonWebServiceRequest,
                        exception: AmazonClientException,
                        retriesAttempted: Int
                      ): Option[Long] = exception match {

          case _: ItemCollectionSizeLimitExceededException =>
            Some(expMax(baseDelay, retriesAttempted + 1, maxDelay))

          case _: LimitExceededException =>
            Some(expMax(baseDelay, retriesAttempted + 1, maxDelay))

          case _: ProvisionedThroughputExceededException =>
            Some(expMax(throttledDelay, retriesAttempted + 1, throttledMax))

          case _ => None
        }

        def maxErrorRetry: Int = maxRetry
      }
    }
  }

  case class TableConfig(
                          tableName: String,
                          regionName: Option[String] = None,
                          credentials: Option[(String, String)] = None,
                          clientConfig: Option[ClientConfig] = None
                        ) {

    def withRegionName(region: String): TableConfig =
      this.copy(regionName = Some(region))

    def withCredentials(accessKeyId: String, secretKey: String): TableConfig =
      this.copy(credentials = Some((accessKeyId, secretKey)))

    def withClientConfig(cfg: ClientConfig): TableConfig =
      this.copy(clientConfig = Some(cfg))

    def toTable: DynamoDBTableContract = {

      import com.amazonaws.ClientConfiguration
      import com.amazonaws.auth.{AWSCredentials, AWSCredentialsProvider}
      import com.amazonaws.regions.Regions
      import com.amazonaws.retry.RetryPolicy
      import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder

      val builder = AmazonDynamoDBClientBuilder.standard()

      builder.setRegion(regionName.getOrElse(Regions.US_WEST_1.getName))

      credentials.foreach { creds =>

        val (aKey, sKey) = creds

        val awsCredentials = new AWSCredentialsProvider {
          def refresh(): Unit = ()
          def getCredentials: AWSCredentials = new AWSCredentials {
            def getAWSAccessKeyId: String = aKey
            def getAWSSecretKey: String = sKey
          }
        }

        builder.setCredentials(awsCredentials)
      }

      clientConfig.foreach { cfg =>

        val (delay, max) = (cfg.retryDelay(_, _, _), cfg.maxErrorRetry)

        val cond = new RetryPolicy.RetryCondition {
          def shouldRetry(
                           originalRequest: AmazonWebServiceRequest,
                           exception: AmazonClientException,
                           retriesAttempted: Int
                         ): Boolean =
            delay(originalRequest, exception, retriesAttempted).isDefined
        }

        val strat = new RetryPolicy.BackoffStrategy {
          def delayBeforeNextRetry(
                                    originalRequest: AmazonWebServiceRequest,
                                    exception: AmazonClientException,
                                    retriesAttempted: Int
                                  ): Long =
            delay(originalRequest, exception, retriesAttempted).getOrElse(0)
        }

        val retryPolicy = new RetryPolicy(cond, strat, max, true)

        val awsConf = new ClientConfiguration()
          .withRetryPolicy(retryPolicy)
          .withMaxErrorRetry(max)

        builder.setClientConfiguration(awsConf)
      }

      val table = new DynamoDB(builder.build()).getTable(tableName)

      DynamoDBTableContract.fromSDKTable(table)
    }
  }
}
