package io.github.massimosiani.process

import io.github.massimosiani.process.ProcState.{Complete, NoRequest, Sent}
import io.github.massimosiani.process.Process.*

type Service[A] = Process[A, NoRequest, Complete]
type Client[A] = Process[A, NoRequest, NoRequest]

val procAdder: Service[Unit] = for {
  _ <- Process.Respond(_ match {
    case Message.Add(x, y) => Process.Pure(x + y)
  })
  _ <- Process.Action(())
  _ <- Process.Action(())
  _ <- Process.Loop(procAdder)
} yield ()

val procMain: Client[Unit] = for {
  maybeAdderPid <- Spawn(procAdder)
  _ <- maybeAdderPid match {
    case Some(adderPid) =>
      for {
        answer <- Request(adderPid, Message.Add(2, 3))
        _ <- Action(println(answer))
      } yield ()
    case None => Action(println("Spawn failed"))
  }
} yield ()

@main def main(args: String*): Unit =
  println(ConsoleProcessInterpreter.run(procMain))
