package com.github.monogram.gameserver

import com.github.monogram.{Board, BoardElement, PuzzleReader, SideMetadataElement}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

import scala.collection.mutable

class GameController extends Controller {

  val puzzlesMapping: mutable.Map[String, Board] = mutable.Map()

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
    val puzzleId = request.params.getOrElse("puzzleId", "chopin_nocturne_9_2")
    val puzzleBoard = puzzlesMapping.getOrElse(puzzleId, PuzzleReader.readPuzzle(puzzleId))
    if(!puzzlesMapping.contains(puzzleId)){
      puzzlesMapping(puzzleId) = puzzleBoard
    }
    val metadata = puzzleBoard.buildBoardMetadata()
    BoardResponse(board = puzzleBoard.boardConfig,
      rows = puzzleBoard.boardRows,
      cols = puzzleBoard.boardCols,
      rowsMetadata = metadata._1,
      colsMetadata = metadata._2)
  }

}