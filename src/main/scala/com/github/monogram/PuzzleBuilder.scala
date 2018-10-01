package com.github.monogram

import java.io.{File, PrintWriter}

import scala.math.sqrt

object PuzzleBuilder {

  def calculateBoardSize(elements: List[MusicalElement]): (Int, Int) ={
    val minDuration = elements.minBy(x => x.duration).duration
    assert(minDuration>0)

    val startTime = elements.head.startTime
    val endTime = elements.last.startTime + elements.last.duration
    val totalDuration = endTime - startTime

    val squaresCount: Double = totalDuration.toDouble / minDuration

    val sideSize = sqrt(squaresCount).ceil.toInt
    (sideSize, sideSize)
  }

  def persistToFile(board: Board, filePath: String): Unit = {
    val pw = new PrintWriter(new File(filePath))
    pw.write(s"${board.boardRows},${board.boardCols}\n")
    for(i <- 0 until board.boardRows){
      for(j <- 0 until board.boardCols){
        pw.write(s"$i,$j,${board.getColor(i,j).toHex}\n")
      }
    }
    pw.close()
  }

}
