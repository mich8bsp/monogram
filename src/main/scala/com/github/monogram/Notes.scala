package com.github.monogram

object Note extends Enumeration {
  type Note = Value
  val C, C_SH, D, D_SH, E, F, F_SH, G, G_SH, A, A_SH, B = Value

  def fromPitch(pitch: Int): Note = {
    // 21 is the pitch of C0
    Note.values.toList((pitch - 21) % 12)
  }
}