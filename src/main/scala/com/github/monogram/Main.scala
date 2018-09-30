package com.github.monogram

import com.github.monogram.Note.Note
import org.scalajs.dom
import dom.document
import org.scalajs.dom.raw.HTMLElement

object Main {



  def main(args: Array[String]): Unit = {
//    val board = document.createElement("p")
//    board.textContent = "board placeholder"
//    document.body.appendChild(board)

    val (originalSeq, tracks) = MIDIParser.parse("midis/chopin_nocturne_9_2.mid")
    println(tracks.size)
    val notesList: List[Note] = MIDIParser.convertAndMapMusic(tracks(0))

    notesList.foreach(println)
  }
}
