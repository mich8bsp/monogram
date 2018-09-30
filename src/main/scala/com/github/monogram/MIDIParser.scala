package com.github.monogram

import de.sciss.midi.MetaMessage.EndOfTrack
import de.sciss.midi._

object MIDIParser {

  def parse(midiPath: String): (Sequence, Map[Int, Track]) = {
    val sq = Sequence.read(midiPath)

    def hasMusic(track: Track): Boolean = {
      track.events.exists(e => e.message match {
        case NoteOn(_, _, _) => true
        case _ => false
      })
    }

    val tracksWithMusic = sq.tracks.filter(hasMusic)

    implicit val tickRate: TickRate = TickRate(1)

    val channels: Map[Int, Track] = tracksWithMusic.flatMap(_.events)
      .groupBy(event => event.message match {
        case NoteOn(channel, _, _) => channel
        case NoteOff(channel, _, _) => channel
        case _ => -1
      })
      .filter(_._1>=0)
      .mapValues(Track(_))

    (sq, channels)
  }

  def convertTrack(track: Track): List[MusicalElement]= {

    def updateWithDuration(element: MusicalElement, stopTime: Long): MusicalElement = element match {
      case Rest(startTime, _) => Rest(startTime, stopTime - startTime)
      case GameNote(note, startTime, _) => GameNote(note, startTime, stopTime - startTime)
    }

    def isMatchingPitch(element: MusicalElement, pitch: Int): Boolean = element match {
      case Rest(_,_) => false
      case GameNote(note, _, _) => Note.fromPitch(pitch) == note
    }

    def convertNotes(remainingNotes: List[Event], lastNote: MusicalElement): List[MusicalElement] = remainingNotes match {
      case Nil => List()
      case x::xs => x match {
        case Event(time, NoteOn(_, pitch, _)) =>
          if(lastNote==null){
            //if first note we should wait for second to calculate duration
            convertNotes(xs, GameNote(Note.fromPitch(pitch), time, 0))
          }else{
            //if new note is played we should stop the previous note/rest and update its duration
            updateWithDuration(lastNote, time)::convertNotes(xs, GameNote(Note.fromPitch(pitch), time, 0))
          }
        case Event(time, NoteOff(_, pitch, _)) =>
          if(lastNote!=null && isMatchingPitch(lastNote, pitch)){
            //note off means we should stop the last note and update duration, the next element is rest until the next notes comes
            updateWithDuration(lastNote, time)::convertNotes(xs, Rest(time, 0))
          }else{
            // note on after note on will act as note off for the first one, which means we can disregard its note-off
            convertNotes(xs, lastNote)
          }

        case Event(time, EndOfTrack) => updateWithDuration(lastNote, time)::convertNotes(xs, Rest(time, 0))
      }
    }

    convertNotes(track.events.toList, null)
      .filter(_.duration>0)

  }

  def main(args: Array[String]): Unit = {
    val (originalSequence, channels) = parse("midis/chopin_nocturne_9_2.mid")

    val pl = Sequencer.open()

//    channels.values
//      .map(x => Sequence(scala.collection.immutable.IndexedSeq(x)))
//      .foreach(pl.play)

    pl.play(originalSequence)
  }
}