package tastytest

import scala.language.experimental.macros

object MacroCompat {

  implicit def pos: Position = macro Macros.posImpl // implemented in test/tasty/run/pre/tastytest/package.scala
  implicit inline def pos: Position = ${ Macros3.posImpl }

  def testCase(test: => Any)(using Position): String =
    s"${String.valueOf(test)} @@ ${summon[Position]}"

  object Macros3 {
    import quoted._

    def posImpl(using qctx: QuoteContext): Expr[Position] = {
      import qctx.tasty.{given _}
      val name = qctx.tasty.rootPosition.sourceFile.jpath.getFileName.toString
      val line = qctx.tasty.rootPosition.startLine + 1
      '{ Position(${Expr(name)}, ${Expr(line)}) }
    }
  }

}
