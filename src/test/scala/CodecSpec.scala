import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks._
import boolexpr._
import boolexpr.Codec._
import io.circe.syntax._
import io.circe.parser._
import io.circe.{Json}

class CodecSpec extends AnyFunSpec {

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

  def constJson(b: Boolean): String = s"""{ "type": "const", "value": $b }"""
  def notJson(x: String): String = s"""{ "type": "not", "value": $x }"""
  def varJson(name: String): String =
    s"""{ "type": "var", "value": ${name.asJson.noSpaces} }"""
  def opJson(typ: String, e1: String, e2: String): String =
    s"""{ "type": "$typ", "value": [$e1,$e2] }"""



  describe("Encoding") {
    describe("of constant expressions") {
      it("should produce the expected Json") {
        val t = Right(toJson(True))
        val x = parse(constJson(true))
        assert(t == x)
        val f = Right(toJson(False))
        val y = parse(constJson(false))
        assert(f == y)
      }
    }

    describe("of variables") {
      it("should produce the expected Json for all variable names") {
        forAll ("name") { (name: String) => {
            val v = Right(toJson(Variable(name)))
            val x = parse(varJson(name))
            assert(v == x)
          }
        }
      }
    }

    describe("of operations") {
      it("should produce the expected type field") {
        val not = toJson(Not(True))
        assert(not.hcursor.downField("type").as[String] == Right("not"))
        val and = toJson(And(True, False))
        assert(and.hcursor.downField("type").as[String] == Right("and"))
        val or = toJson(Or(True, False))
        assert(or.hcursor.downField("type").as[String] == Right("or"))
      }

      it("should produce the expected value field") {
        val notVal = toJson(Not(True)).hcursor.downField("value").as[Json]
        assert(notVal == parse(constJson(true)))
        val andVal = toJson(And(True, False)).hcursor.downField("value").as[Json]
        assert(andVal == parse(s"[${constJson(true)},${constJson(false)}]"))
        val orVal = toJson(Or(True, False)).hcursor.downField("value").as[Json]
        assert(orVal == parse(s"[${constJson(true)},${constJson(false)}]"))
      }
    }
  }

  describe("Decoding") {
    describe("of constant expressions") {
      it("should produce the expected value") {
        val t = Right(True)
        val x = parse(constJson(true)).right.get.as[BooleanExpression]
        assert(t == x)
        val f = Right(False)
        val y = parse(constJson(false)).right.get.as[BooleanExpression]
        assert(f == y)
      }
    }

    describe("of variables") {
      it("should produce the expected value for all variable names") {
        forAll ("name") { (name: String) => {
            val v = Right(Variable(name))
            val x = parse(varJson(name)).right.get.as[BooleanExpression]
            assert(v == x)
          }
        }
      }
    }

    describe("of operations") {
      it("should produce the expected value") {
        val notVal = parse(notJson(constJson(true))).right.get.as[BooleanExpression]
        assert(notVal == Right(Not(True)))
        val andVal = parse(opJson("and", constJson(true), constJson(false))).right.get.as[BooleanExpression]
        assert(andVal == Right(And(True,False)))
        val orVal = parse(opJson("or", constJson(true), constJson(false))).right.get.as[BooleanExpression]
        assert(orVal == Right(Or(True,False)))
      }
    }

    it("should roundtrip") {
      assert(allExprTypes.asJson.as[BooleanExpression] == Right(allExprTypes))
      assert(deepTree.asJson.as[BooleanExpression] == Right(deepTree))
    }
  }

}

