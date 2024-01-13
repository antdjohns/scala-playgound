package com.ossync.jobsboard.config

import pureconfig.ConfigSource
import cats.MonadThrow
import pureconfig.ConfigReader

object syntax {
  extension (source: ConfigSource)
    def loadF[F[_], A](using reader: ConfigReader[A], F: MonadThrow[F]): F[A] = ???
}