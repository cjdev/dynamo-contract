import scala.util.Try

object DynamoContractDemo extends App {

  import com.cj.dynamocontract.ClientConfig
  import com.cj.dynamocontract.DynamoDBTableContract
  import com.cj.dynamocontract.TableConfig

  // Create a config with just the table name to use the default
  // region (US-WEST-1) and pull credentials from the environment
  val tableWithDefaults: Try[DynamoDBTableContract] = {
    val tableName: String = "foo"
    TableConfig(tableName).getTable
  }
  println(tableWithDefaults)

  // Create a config with the specified credentials
  val tableWithCredentials: Try[DynamoDBTableContract] = {
    val tableName: String = "foo"
    val accessKeyId: String = "bar"
    val secretKey: String = "baz"
    TableConfig(tableName).withCredentials(accessKeyId, secretKey).getTable
  }
  println(tableWithCredentials)

  // ... or with the specified region
  val tableWithRegion: Try[DynamoDBTableContract] = {
    val tableName: String = "foo"
    val regionName: String = "bar"
    TableConfig(tableName).withRegionName(regionName).getTable
  }
  println(tableWithRegion)

  // To create a table with a specific backoff strategy, create a
  // ClientConfig and feed it to your TableConfig
  val tableWithBackoff: Try[DynamoDBTableContract] = {
    val tableName: String = "foo"

    val clientConf: ClientConfig = new ClientConfig {

      import com.amazonaws.{AmazonClientException, AmazonWebServiceRequest}

      // Specify the logic that determines if you
      // will retry how long to wait if you do
      def retryDelay(
                      originalRequest: AmazonWebServiceRequest,
                      exception: AmazonClientException,
                      retriesAttempted: Int
                    ): Option[Long] = None

      // Specify your desired max number of retries
      def maxErrorRetry: Int = 0
    }

    TableConfig(tableName).withClientConfig(clientConf).getTable
  }
  println(tableWithBackoff)

  // We provide a constructor that simplifies ClientConfig
  // creation if you want exponential backoff specifically
  val tableWithExponentialBackoff: Try[DynamoDBTableContract] = {
    val tableName: String = "foo"

    // The numbers specified here are the default for the AWS Java SDK
    // as well as the default for this same constructor method
    val clientConf: ClientConfig =
      ClientConfig.exponentialBackoff(
        baseDelay = 500,
        maxDelay = 20000,
        throttledDelay = 25,
        throttledMax = 20000,
        maxRetry = 10
      )

    TableConfig(tableName).withClientConfig(clientConf).getTable
  }
  println(tableWithExponentialBackoff)
}
