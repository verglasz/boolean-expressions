package boolexpr

import io.circe.{Encoder, Decoder, Error}
import io.circe.parser.parse
import io.circe.syntax._
// using semiauto instead of auto to work around what seems to be a bug in circe
// see https://github.com/circe/circe/issues/1910
import io.circe.generic.semiauto._


type DeserializationResult = Either[Error, BooleanExpression]

object Codec {
  implicit val expressionDecoder: Decoder[BooleanExpression] = deriveDecoder[BooleanExpression]
  implicit val expressionEncoder: Encoder[BooleanExpression] = deriveEncoder[BooleanExpression]
}

/**
 * Return the serialized (json string) representation of a BooleanExpression
 */
def serialize(expr: BooleanExpression): String = {
  import Codec._
  expr.asJson.noSpaces
}


/**
 * Return either the deserialized BooleanExpression or, if the string isn't
 * a valid serialized representation of one, a circe error
 */
def deserialize(s: String): DeserializationResult = {
  import Codec._
  parse(s).flatMap(_.as[BooleanExpression])
}

