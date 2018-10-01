package com.github.monogram.gameserver

import com.github.monogram.{Board, BoardElement, PuzzleReader, SideMetadataElement}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

import scala.collection.mutable

class GameController extends Controller {

  case class Puzzle(id: Int, name: String, solution: Board)

  val puzzlesMapping: mutable.Map[Int, Puzzle] = mutable.Map()

  //put all puzzles here
  val puzzleIdToName: Map[Int, String] = Map(1 -> "chopin_nocturne_9_2")

  case class BoardResponse(board: Array[Array[BoardElement]],
                           rows: Int,
                           cols: Int,
                           rowsMetadata: List[List[SideMetadataElement]],
                           colsMetadata: List[List[SideMetadataElement]])

  get("/:*") { request: Request =>
    println(s"got request $request")
    response.ok.fileOrIndex(
      request.params("*"),
      "/client/index.html")
  }

  get("/getPuzzle") { request: Request =>
    val puzzleId = request.params.getOrElse("puzzleId", "1").toInt
    val puzzleName = puzzleIdToName(puzzleId)
    val puzzle = puzzlesMapping.getOrElse(puzzleId, Puzzle(puzzleId, puzzleName, PuzzleReader.readPuzzle(puzzleName)))
    if(!puzzlesMapping.contains(puzzleId)){
      puzzlesMapping(puzzleId) = puzzle
    }
    val metadata = puzzle.solution.buildBoardMetadata()
    BoardResponse(board = puzzle.solution.boardConfig,
      rows = puzzle.solution.boardRows,
      cols = puzzle.solution.boardCols,
      rowsMetadata = metadata._1,
      colsMetadata = metadata._2)
  }

}