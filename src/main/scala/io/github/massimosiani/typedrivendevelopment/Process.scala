package io.github.massimosiani.typedrivendevelopment

import io.github.massimosiani.typedrivendevelopment.ProcState._

enum Process[A, InState <: ProcState, OutState <: ProcState]:
  case Request(pid: MessagePID, msg: Message) extends Process[Int, InState, InState]
  case Respond[State <: ProcState](f: Message => Process[Int, NoRequest, NoRequest])
      extends Process[Option[Message], State, Sent]
  case Spawn[State <: ProcState](p: Process[Unit, NoRequest, Complete])
      extends Process[Option[MessagePID], State, State]
  case Loop[A](p: Process[A, NoRequest, Complete]) extends Process[A, Sent, Complete]
  case Action[A, State <: ProcState](ioa: A) extends Process[A, State, State]
  case Pure[A, State <: ProcState](a: A) extends Process[A, State, State]
  case >>=[A, St1 <: ProcState, St2 <: ProcState, B, St3 <: ProcState](
      p: Process[A, St1, St2],
      f: A => Process[B, St2, St3],
    ) extends Process[B, St1, St3]

  def map[B](f: A => B): Process[B, InState, OutState] = flatMap(a => Pure(f(a)))
  def flatMap[B, St1 <: ProcState](f: A => Process[B, OutState, St1]): Process[B, InState, St1] =
    >>=(this, f)

case class MessagePID(pid: String)

enum Message:
  case Add(x: Int, y: Int)
