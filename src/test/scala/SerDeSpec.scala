import org.scalatest.funspec.AnyFunSpec

import boolexpr._
import io.circe.parser._
import io.circe.syntax._

class SerDeSpec extends AnyFunSpec {
  describe("Serialization") {
    it("should produce valid JSON") {
      assert(parse(serialize(True)).isRight)
    }
  }

  describe("Deserialization") {
    it("should give an error for an empty string") {
      assert(deserialize("").isLeft)
    }
  }
}

