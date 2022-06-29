import org.scalatest.funspec.AnyFunSpec
import org.scalatest.propspec.AnyPropSpec

import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks._

import boolexpr._
import io.circe.parser._
import io.circe.syntax._
import io.circe._



class SerDeSpec extends AnyPropSpec {
  val allExprTypes: BooleanExpression = And(Not(Variable("A")),Or(True,False));
  val deepTree: BooleanExpression =
    Not(Or(
      And(Not(allExprTypes), Variable("B")),
      Not(And(
        Or(allExprTypes, Variable("A")),
        And(
          Or(False, Variable("X")),
          And(True, allExprTypes)
        )
      ))
    ))

  property("Serialization produces valid JSON") {
    assert(parse(serialize(True)).isRight)
    assert(parse(serialize(allExprTypes)).isRight)
    assert(parse(serialize(deepTree)).isRight)
  }

  property("Deserialization gives a parsing error for invalid JSON") {
    forAll ("input") { (input: String) => {
      whenever(parse(input).isLeft) {
        assert(deserialize(input).isLeft)
        assert(deserialize(input).left.get.isLeft)
      }
    }}
  }

}

