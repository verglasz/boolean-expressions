package boolexpr

import io.circe.{ Decoder, Encoder, HCursor, Json, DecodingFailure, CursorOp }
import io.circe.syntax.EncoderOps

/**
 * Provides Circe codec (Encoder/Decoder) for converting BooleanExpression to/from Json
 */
object Codec {

  implicit val encodeExpression: Encoder[BooleanExpression] = new Encoder[BooleanExpression] {
    final def apply(e: BooleanExpression): Json = {
      val (type_, value) = e match {
        case True => ("const", true.asJson)
        case False => ("const", false.asJson)
        case Variable(name) => ("var", name.asJson)
        case Not(e1) => ("not", e1.asJson)
        case And(e1, e2) => ("and", (e1,e2).asJson)
        case Or(e1, e2) => ("or", (e1,e2).asJson)
      }
      Json.obj(("type", type_.asJson), ("value" , value))
    }
  }

  private type ExpressionPair = (BooleanExpression, BooleanExpression)

  implicit val decodeExpression: Decoder[BooleanExpression] = new Decoder[BooleanExpression] {
    final def apply(c: HCursor): Decoder.Result[BooleanExpression] = {
      val value = c.downField("value")
      c.downField("type").as[String].flatMap( _ match {
        case "const" => value.as[Boolean].map(if _ then True else False)
        case "var" => value.as[String].map(Variable(_))
        case "not" => value.as[BooleanExpression].map(Not(_))
        case "and" => value.as[ExpressionPair].map(es => And(es._1, es._2))
        case "or" => value.as[ExpressionPair].map(es => Or(es._1, es._2))
        case other => {
          val message = s"""Unrecognized expression type "$other""""
          val ops = c.history.appended(CursorOp.DownField("type"))
          Left(DecodingFailure(message, ops))
        }
      })
    }
  }

  // Convenience function to ergonomically convert to Json bare case classes
  // (as for example Not(True).asJson doesn't work, since the enoder
  // implementation is specifically for BooleanExpression (and invariant),
  // thus requiring (Not(True): BooleanExpression).asJson, which is
  // less nice than simply toJson(Not(True)), letting the invocation
  // perform the cast/coercion)
  def toJson(expr: BooleanExpression): Json = expr.asJson

  // simmetric conversion function
  def fromJson(j: Json): Decoder.Result[BooleanExpression] = j.as[BooleanExpression]

}

