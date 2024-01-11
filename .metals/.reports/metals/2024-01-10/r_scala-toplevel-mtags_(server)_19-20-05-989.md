error id: file://<WORKSPACE>/src/main/scala/com/ossync/jobsboard/http/routes/HealthRoutes.scala:[509..509) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/com/ossync/jobsboard/http/routes/HealthRoutes.scala", "package com.ossync.jobsboard.http.routes

import cats.*
import org.http4s.* 
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.circe.*
import org.http4s.ember.server.*
import org.http4s.server.*

class HealthRoutes[F[_]: Monad] extends Http4sDsl[F] {
    def healthRoute: HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case GET -> Root / "health" => 
                Ok("looking good")
        }
    }

    val routes = Router(
        "/health" -> healthRoute
    )
}

object 
")
file://<WORKSPACE>/src/main/scala/com/ossync/jobsboard/http/routes/HealthRoutes.scala
file://<WORKSPACE>/src/main/scala/com/ossync/jobsboard/http/routes/HealthRoutes.scala:25: error: expected identifier; obtained eof

^
#### Short summary: 

expected identifier; obtained eof