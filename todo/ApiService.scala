package dev.nhyne.todo


import dev.nhyne.todo.persistence.TodoItemPersistenceService
import zio.RIO

import org.http4s.circe._
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import persistence.TodoItemPersistenceService._


final case class ApiService[R <: TodoItemPersistenceService](rootUri: String) {
    type TodoTask[A] = RIO[R, A]

    implicit def circeJsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[TodoTask, A] = jsonOf[TodoTask, A]
    implicit def circeJsonEncoder[A](implicit decoder: Encoder[A]): EntityEncoder[TodoTask, A] = jsonEncoderOf[TodoTask, A]

    val dsl = Http4sDsl[TodoTask]
    import dsl._

    def route: HttpRoutes[TodoTask] = {
        case GET -> Root / IntVar => get
    }
}
