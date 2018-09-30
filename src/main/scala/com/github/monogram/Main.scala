package com.github.monogram

import org.scalajs.dom
import dom.document
import org.scalajs.dom.raw.HTMLElement

object Main {



  def main(args: Array[String]): Unit = {
    val board = document.createElement("p")
    board.textContent = "board placeholder"
    document.body.appendChild(board)
  }
}
