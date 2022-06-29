import boolexpr._
import scala.io.StdIn.readLine
import System.err.println as eprintln

@main
def main(): Unit = {

  val input = Iterator.continually(readLine).takeWhile(_ != null).mkString("\n")
  deserialize(input) match {
    case Left(err) => {
      eprintln("Ihe input is not a valid serialized BooleanExpression:")
      eprintln(err)
    }
    case Right(exp) => {
      eprintln("Obtained expression:")
      eprintln(exp)
      println(serialize(exp))
    }
  }
}

