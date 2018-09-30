package com.github.monogram

import com.github.monogram.Note._
import org.scalajs.dom.ext.Color

class Board(boardRows: Int, boardCols: Int){

  private var boardConfig = Array.ofDim[BoardElement](boardRows, boardCols)
  val boardSize = boardRows * boardCols

  case class BoardElement(color: Color)

  def boardElementToColor(element: MusicalElement): Color = element match {
    case x: GameNote => x.note match {
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

  def buildBoard(elements: List[MusicalElement]): Unit ={
    val startTime = elements.head.startTime
    val endTime = elements.last.startTime + elements.last.duration
    val totalDuration = endTime - startTime

    def counterToRowCol(counter: Int): (Int, Int) = (counter / boardCols, counter - (counter / boardCols)*boardCols)

    def durationToNumOfSquares(duration: Long): Int = ((duration.toDouble / totalDuration) * boardSize).ceil.toInt

    def fillBoard(remainingToFill: List[MusicalElement], currentBoardCounter: Int): Unit = remainingToFill match {
      case x::xs =>
        val duration = x.duration
        val squaresToFill = durationToNumOfSquares(duration)
        println(s"at counter $currentBoardCounter filling $squaresToFill squares")
        val color = boardElementToColor(x)

        if(squaresToFill>0){
          Range(currentBoardCounter, currentBoardCounter + squaresToFill).foreach(i => {
            val (r, c) = counterToRowCol(i)
            boardConfig(r)(c) = BoardElement(color)
          })
        }
        fillBoard(xs, currentBoardCounter + squaresToFill)
      case Nil =>
    }

    fillBoard(elements, 0)
  }

  def clearBoard = boardConfig = Array.ofDim[BoardElement](boardRows, boardCols)

  def getColor(r: Int, c: Int) = boardConfig(r)(c).color


}

