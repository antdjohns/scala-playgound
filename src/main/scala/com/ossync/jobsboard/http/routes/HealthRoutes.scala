package com.ossync.jobsboard.http.routes

import cats.*
import org.http4s.* 
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.circe.*
import org.http4s.ember.server.*
import org.http4s.server.*
import cats.effect.*

class HealthRoutes[F[_]: Concurrent] extends Http4sDsl[F] {
    private def healthRoute: HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case GET -> Root  => 
                Ok("looking good")
        }
    }

    val routes = Router(
        "/health" -> healthRoute
    )
}

object HealthRoutes {
    def apply[F[_]: Concurrent] = new HealthRoutes[F]
}
