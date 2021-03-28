case class MessagePID(pid: String)

enum Message {
    case Add(x: Int, y: Int)
}

enum ProcState {
    case NoRequest()
    case Sent()
    case Complete()
}

import ProcState._

enum Process[A, InState <: ProcState, OutState <: ProcState] {
    case Request(pid: MessagePID, msg: Message) extends Process[Int, InState, InState]
    case Respond[State <: ProcState](f: Message => Process[Int, NoRequest, NoRequest]) extends Process[Option[Message], State, Sent]
    case Spawn[State <: ProcState](p: Process[Unit, NoRequest, Complete]) extends Process[Option[MessagePID], State, State]
    case Loop[A](p: Process[A, NoRequest, Complete]) extends Process[A, Sent, Complete]
    case Action[A, State <: ProcState](ioa: A) extends Process[A, State, State]
    case Pure[A, State <: ProcState](a: A) extends Process[A, State, State]
    case >>=[A, St1 <: ProcState, St2 <: ProcState, B, St3 <: ProcState](p: Process[A, St1, St2], f : A => Process[B, St2, St3]) extends Process[B, St1, St3]

    def map[B](f: A => B): Process[B, InState, OutState] = flatMap(a => Pure(f(a)))
    def flatMap[B, St1 <: ProcState](f: A => Process[B, OutState, St1]): Process[B, InState, St1] = >>=(this, f)
}
object Process {
    def run[A, InState <: ProcState, OutState <: ProcState](p : Process[A, InState, OutState]): Option[A] = p match {
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
        case Pure(v) => Option(v)
        case >>=(p, next) => run(p).flatMap(x => run(next(x)))
    }
}

import Process._

type Service[A] = Process[A, NoRequest, Complete]
type Client[A] = Process[A, NoRequest, NoRequest]

val procAdder : Service[Unit] = for {
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
        case Some(adderPid) => for {
            answer <- Request(adderPid, Message.Add(2, 3))
            _ <- Action(println(answer))
        } yield ()
        case None => Action(println("Spawn failed"))
    }
} yield ()

@main def main(args: String*): Unit =
    println(Process.run(procMain))
