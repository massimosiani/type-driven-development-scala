package io.github.massimosiani.elevator

import cats.Show
import cats.syntax.all.*

import DoorState.*
import SDoorState.*

import scala.compiletime.erasedValue

enum DoorState:
  case Opened
  case Closed

object DoorState:
  given Show[DoorState] = new Show[DoorState] {
    def show(d: DoorState) = d match {
      case Opened => "Opened"
      case Closed => "Closed"
    }
  }

  given SDoorStateI[Opened.type] = new SDoorStateI[Opened.type] {
    override def sDoorState = SOpened
  }
  given SDoorStateI[Closed.type] = new SDoorStateI[Closed.type] {
    override def sDoorState = SClosed
  }

enum SDoorState[S <: DoorState]:
  case SClosed extends SDoorState[Closed.type]
  case SOpened extends SDoorState[Opened.type]

trait SDoorStateI[S <: DoorState]:
  def sDoorState: SDoorState[S]

enum Door[S <: DoorState]:
  case MkDoor()(using foo: SDoorStateI[S]) extends Door[S]

  inline def doorState: DoorState = inline erasedValue[S] match
    case _ : Opened.type => Opened
    case _ : Closed.type => Closed

    given Show[Opened.type] with
    def show(d: Door[S]) = ???