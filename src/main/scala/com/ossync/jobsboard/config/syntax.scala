package com.ossync.jobsboard.config

import pureconfig.ConfigSource
import cats.MonadThrow
import pureconfig.ConfigReader
import cats.implicits.*
import com.typesafe.config.ConfigException
import pureconfig.error.ConfigReaderException
import scala.reflect.ClassTag

object syntax {
  extension (source: ConfigSource)
    def loadF[F[_], A](using reader: ConfigReader[A], F: MonadThrow[F], tag: ClassTag[A]): F[A] = {
        F.pure(source.load[A]).flatMap {
            case Left(errors) => F.raiseError[A](ConfigReaderException(errors))
            case Right(value) => F.pure(value)
        }
    }
}