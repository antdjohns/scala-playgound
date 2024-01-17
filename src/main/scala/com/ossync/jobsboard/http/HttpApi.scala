package com.ossync.jobsboard.http

import cats.*
import cats.implicits.*
import org.http4s.* 
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.circe.*
import org.http4s.ember.server.*
import org.http4s.server.*
import com.ossync.jobsboard.http.routes.*
import cats.effect.*

class HttpApi[F[_]: Concurrent] private {
  private val healthRoutes= HealthRoutes[F].routes
  private val jobRoutes = JobsRoutes[F].routes

  val endpoints  = Router(
    "/api" -> (healthRoutes <+> jobRoutes)
  )
}

object HttpApi {
    def apply[F[_]: Concurrent] = new HttpApi[F]
}
