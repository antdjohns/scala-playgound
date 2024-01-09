package com.ossync.jobsboard.playground

import cats.*
import cats.effect.{IO, IOApp}
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.ember.server.*
import org.http4s.server.*

/*
*
* */

object Application  {
//  def healthEndpoint[F[_]: Monad]: HttpRoutes[F] = {
//    val dsl = Http4sDsl[F]
//    import dsl.*
//    HttpRoutes.of[F] {
//      case GET -> Root / "health" => Ok("All going great")
//    }
//  }
//
//  def allRoutes[F[_]: Monad]: HttpRoutes[F] = courseRoutes[F] <+> healthEndpoint[F]
//
//  def routerWithPathPrefixes = Router(
//    "/api" -> courseRoutes[IO],
//    "/private" -> healthEndpoint[IO]
//  ).orNotFound
//
//  override def run = EmberServerBuilder
//    .default[IO]
//    .withHttpApp(routerWithPathPrefixes)
//    .build
//    .use(_ => IO.println("Server ready!!!") *> IO.never)


  def main(args: Array[String]): Unit = {
    println("Ty[elevel Project, here we go")
  }
}
