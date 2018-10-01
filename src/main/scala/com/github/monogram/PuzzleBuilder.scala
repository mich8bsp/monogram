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
        pw.write(s"$i,$j,${board.getColor(i,j).toHex},${board.getText(i,j)}\n")
      }
    }
    pw.close()
  }

    def main(args: Array[String]): Unit = {
  //    val board = document.createElement("p")
  //    board.textContent = "board placeholder"
  //    document.body.appendChild(board)

      val puzzleName = "chopin_nocturne_9_2"
      val (originalSeq, tracks) = MIDIParser.parse(s"midis/$puzzleName.mid")
      val notesList: List[MusicalElement] = MIDIParser.convertTrack(tracks(0))
  //    notesList.foreach(println)

      val (boardRows, boardCols) = calculateBoardSize(notesList)

      println(s"building board of size $boardRows X $boardCols")

      val board = new Board(boardRows = boardRows ,boardCols = boardCols)
      board.buildBoard(notesList)

      for(i <- 0 until boardRows){
        for(j <- 0 until boardCols){
          println(s"($i,$j) = ${board.getColor(i, j)}")
        }
      }


      persistToFile(board, s"puzzles/$puzzleName.txt")

    }

}
