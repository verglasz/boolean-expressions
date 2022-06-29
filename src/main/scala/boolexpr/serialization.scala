package boolexpr

import io.circe.{ParsingFailure, DecodingFailure}
import io.circe.syntax.EncoderOps
import io.circe.parser.parse

import boolexpr.Codec._


/**
 * Return the serialized (json string) representation of a BooleanExpression
 */
def serialize(expr: BooleanExpression): String = expr.asJson.noSpaces

type DeserializationResult = Either[Either[ParsingFailure, DecodingFailure], BooleanExpression]

/**
 * Return either the deserialized BooleanExpression or, if the string isn't
 * a valid serialized representation of one, a circe error
 */
def deserialize(s: String): DeserializationResult = {
  parse(s).map(fromJson) match {
    case Left(err) => Left(Left(err))
    case Right(Left(err)) => Left(Right(err))
    case Right(Right(res)) => Right(res)
  }
}

