package io.github.massimosiani.process

import io.github.massimosiani.process.Process.{
  >>=,
  Action,
  Loop,
  Pure,
  Request,
  Respond,
  Spawn,
}

object ConsoleProcessInterpreter:
  def run[A, InState <: ProcState, OutState <: ProcState](
      p: Process[A, InState, OutState]
    ): Option[A] = p match {
    case Request(MessagePID(pid), msg) =>
      println("connecting...")
      println(s"sending message $msg...")
      Option(5)
    case Respond(calc) =>
      val r = calc(Message.Add(2, 3))
      println(s"answering $r")
      Option(Option(Message.Add(2, 3)))
    case Loop(act) => run(act)
    case Spawn(p) =>
      println("spawning...")
      Option(Option(MessagePID("qwertyuiop")))
    case Action(res) =>
      println("Action...")
      Option(res)
    case Pure(v)      => Option(v)
    case >>=(p, next) => run(p).flatMap(x => run(next(x)))
    case null         => None
  }
