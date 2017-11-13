# dynamo-contract

Simple Scala wrapper for aws-java-sdk-dynamodb

## Usage Example

```scala
import com.cj.dynamocontract.{ClientConfig, TableConfig, DynamoDBTableContract}

val yourClientConf = ClientConfig.exponentialBackoff(
  baseDelay = 1000, // one second
  maxDelay = 10000, // ten seconds
  maxRetry = 5 // won't retry more than 5 times
)

val yourTableConf = TableConfig(tableName = "your-table")
  .withRegionName(region = "us-west-1")
  .withCredentials(accessKeyId = "foo", secretKey = "bar")
  .withClientConfig(cfg = yourClientConf)

val ourTable: scala.util.Try[DynamoDBTableContract] =
  DynamoDBTableContract.fromTableConfig(tableConfig = yourTableConf)
```
