package com.github.monogram

import com.github.monogram.Note.Note
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

  def convertAndMapMusic(track: Track): List[Note]= {
    track.events
      .map(_.message)
      .filter(message => message match {
        case NoteOn(_, _, _) => true
        case _ => false
      })
      .map {
        case NoteOn(_, pitch, _) => Note.fromPitch(pitch)
      }
      .toList
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