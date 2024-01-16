package com.ossync.jobsboard

import cats.*
import cats.implicits.*
import cats.effect.{IO, IOApp}
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.ember.server.*
import org.http4s.server.*
import com.comcast.ip4s.*

import com.ossync.jobsboard.http.*

import com.ossync.jobsboard.config.syntax.*
import com.ossync.jobsboard.config.*
import pureconfig.ConfigSource

import pureconfig.error.ConfigReaderException

/*
*
* */

object Application extends IOApp.Simple  {
  /*
    1 - add plain health endpoint
    2 - add minimal configuration
    3 -  basic http server layout
  */

    // def courseRoutes[F[_] : Monad]: HttpRoutes[F] = {
    //   val dsl = Http4sDsl[F]
    //   import dsl.*

    //   HttpRoutes.of[F] {
    //     case GET -> Root / "courses" / "instructor" / instructorName =>
    //       Ok(s"courses for instructor $instructorName")
    //     case GET -> Root / "courses" / "id" / courseId =>
    //       Ok(s"course with id $courseId")
    //   }
    // }

    // def healthEndpoint[F[_]: Monad]: HttpRoutes[F] = {
    //   val dsl = Http4sDsl[F]
    //   import dsl.*
    //   HttpRoutes.of[F] {
    //     case GET -> Root / "health" => Ok("looking good")
    //   }
    // }

    // def allRoutes[F[_]: Monad]: HttpRoutes[F] = courseRoutes[F] <+> healthEndpoint[F]

    val configSource = ConfigSource.default.load[EmberConfig]

    

    override def run = ConfigSource.default.loadF[IO, EmberConfig].flatMap {config => 
          EmberServerBuilder
            .default[IO]
            .withHost(Host.fromString(config.host).get)
            .withPort(Port.fromInt(config.port).get)
            .withHttpApp(HttpApi[IO].endpoints.orNotFound)
            .build
            .use(_ => IO.println("Server Ready") *> IO.never)
    }
      
}