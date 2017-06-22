import org.scalatest.{FlatSpec, Matchers}

class DynamoContractDemoTest extends FlatSpec with Matchers {

  "DynamoContractDemo" should "be up to date" in {
    DynamoContractDemo.main(Array())
  }
}
