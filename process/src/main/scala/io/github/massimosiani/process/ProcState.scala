package io.github.massimosiani.process

enum ProcState:
  case NoRequest()
  case Sent()
  case Complete()
