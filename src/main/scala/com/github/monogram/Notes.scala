package com.github.monogram

import com.github.monogram.Accidental.Accidental
import com.github.monogram.NoteRoot.NoteRoot

case class Note(root: NoteRoot, accidental: Accidental)

object Accidental extends Enumeration{
  type Accidental = Value
  val NATURAL, SHARP, FLAT = Value
}

object NoteRoot extends Enumeration{
  type NoteRoot = Value
  val A, B, C, D, E, F, G  = Value
}