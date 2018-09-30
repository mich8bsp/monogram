package com.github.monogram

import org.scalajs.dom
import dom.document
import org.scalajs.dom.raw.HTMLElement
import scala.math.sqrt

object Main {

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

  def main(args: Array[String]): Unit = {
//    val board = document.createElement("p")
//    board.textContent = "board placeholder"
//    document.body.appendChild(board)

    val (originalSeq, tracks) = MIDIParser.parse("midis/chopin_nocturne_9_2.mid")
    println(tracks.size)
    val notesList: List[MusicalElement] = MIDIParser.convertTrack(tracks(0))
    notesList.foreach(println)

    val (boardRows, boardCols) = calculateBoardSize(notesList)

    println(s"building board of size $boardRows X $boardCols")

    val board = new Board(boardRows = boardRows ,boardCols = boardCols)
    board.buildBoard(notesList)

    for(i <- 0 until boardRows){
      for(j <- 0 until boardCols){
        println(s"($i,$j) = ${board.getColor(i, j)}")
      }
    }
  }
}
