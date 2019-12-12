/*
 * Copyright (C) 2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.actor.typed.internal.delivery

import akka.actor.typed.ActorRef

// Implement with EventSourceBehavior in akka-persistence-typed
// FIXME how much of this should be public? Should it be possible to plug-in different implementation?
object DurableProducerQueue {

  sealed trait Command[A]

  final case class LoadState[A](replyTo: ActorRef[State[A]]) extends Command[A]

  final case class StoreMessageSent[A](sent: MessageSent[A], replyTo: ActorRef[StoreMessageSentAck]) extends Command[A]

  final case class StoreMessageSentAck(confirmedSeqNr: Long)

  final case class StoreMessageConfirmed[A](seqNr: Long) extends Command[A]

  object State {
    def empty[A]: State[A] = State(1L, 0L, Vector.empty)
  }
  final case class State[A](currentSeqNr: Long, confirmedSeqNr: Long, unconfirmed: Vector[MessageSent[A]])

  final case class MessageSent[A](seqNr: Long, msg: A, ack: Boolean)

}