package com.github.monogram

import com.github.monogram.Note.Note

object Note extends Enumeration {
  type Note = Value
  val C, C_SH, D, D_SH, E, F, F_SH, G, G_SH, A, A_SH, B = Value

  def fromPitch(pitch: Int): Note = {
    // 21 is the pitch of C0
    Note.values.toList((pitch - 21) % Note.values.size)
  }
}

trait MusicalElement{
  val startTime: Long
  val duration: Long
}

case class Rest(startTime: Long, duration: Long) extends MusicalElement

case class GameNote(note: Note, startTime: Long, duration: Long) extends MusicalElement