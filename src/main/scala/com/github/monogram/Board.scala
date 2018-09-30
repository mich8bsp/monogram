package com.github.monogram

import com.github.monogram.Note.{A, A_SH, B, C, C_SH, D, D_SH, E, F, F_SH, G, G_SH, Note}
import org.scalajs.dom.ext.Color

class Board(rows: Int, cols: Int){

  private val boardConfig = Array.ofDim[Note](rows, cols)

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

    var squaresCounter = 0
    for(el <- elements){

    }
  }

}

