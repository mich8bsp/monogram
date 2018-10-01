package com.github.monogram

import com.github.monogram.NoteNotation._
import org.scalajs.dom.ext.Color
import scala.math.max

class Board(val boardRows: Int, val boardCols: Int) {

  private var boardConfig = Array.ofDim[BoardElement](boardRows, boardCols)
  val boardSize: Int = boardRows * boardCols

  case class BoardElement(color: Color)

  def musicalElementToColor(element: MusicalElement): Color = element match {
    case x: Note => x.noteNotation match {
      case C => Color.Red
      case C_SH => Color("#d1a835")
      case D => Color("#ff7f19")
      case D_SH => Color("#f9d5d5")
      case E => Color.Yellow
      case F => Color.Green
      case F_SH => Color("#c1e1de")
      case G => Color.Cyan
      case G_SH => Color("#c1e1de")
      case A => Color.Blue
      case A_SH => Color("#602147")
      case B => Color.Magenta
    }
    case _: Rest => Color.White
  }

  def buildBoard(elements: List[MusicalElement]): Unit = {
    val startTime = elements.head.startTime
    val endTime = elements.last.startTime + elements.last.duration
    val totalDuration = endTime - startTime

    def counterToRowCol(counter: Int): (Int, Int) = (counter / boardCols, counter - (counter / boardCols) * boardCols)

    def durationToNumOfSquares(duration: Long): Int = max(((duration.toDouble / totalDuration) * boardSize).floor.toInt, 1)

    def fillBoard(remainingToFill: List[MusicalElement], currentBoardCounter: Int): Int = remainingToFill match {
      case x :: xs =>
        val duration = x.duration
        val squaresToFill = durationToNumOfSquares(duration)
        println(s"at counter $currentBoardCounter filling $squaresToFill squares")
        val color = musicalElementToColor(x)

        if (squaresToFill > 0) {
          Range(currentBoardCounter, currentBoardCounter + squaresToFill).foreach(i => {
            val (r, c) = counterToRowCol(i)
            boardConfig(r)(c) = BoardElement(color)
          })
        }
        fillBoard(xs, currentBoardCounter + squaresToFill)
      case Nil => currentBoardCounter
    }

    fillBoard(elements, 0)
    for (i <- 0 until boardRows) {
      for (j <- 0 until boardCols) {
        if (boardConfig(i)(j) == null) {
          boardConfig(i)(j) = BoardElement(musicalElementToColor(Rest(0, 0)))
        }
      }
    }
  }

  def clearBoard(): Unit = boardConfig = Array.ofDim[BoardElement](boardRows, boardCols)

  def getColor(r: Int, c: Int): Color = boardConfig(r)(c).color

  def setColor(i: Int, j: Int, color: String): Unit = {
    boardConfig(i)(j) = BoardElement(Color(color))
  }



}

