file://<WORKSPACE>/src/main/scala/com/ossync/jobsboard/playground/Application.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 1166
uri: file://<WORKSPACE>/src/main/scala/com/ossync/jobsboard/playground/Application.scala
text:
```scala
package com.ossync.jobsboard.playground

import cats.*
import cats.implicits.*
import cats.effect.{IO, IOApp}
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.ember.server.*
import org.http4s.server.*
import com.comcast.ip4s.*

/*
*
* */

object Application extends IOApp.Simple  {
  /*
    1 - add plain health endpoint
    2 - add minimal configuration
    3 -  basic http server layout
  */

    def courseRoutes[F[_] : Monad]: HttpRoutes[F] = {
      val dsl = Http4sDsl[F]
      import dsl.*

      HttpRoutes.of[F] {
        case GET -> Root / "courses" / "instructor" / instructorName =>
          Ok(s"courses for instructor $instructorName")
        case GET -> Root / "courses" / "id" / courseId =>
          Ok(s"course with id $courseId")
      }
    }

    def healthEndpoint[F[_]: Monad]: HttpRoutes[F] = {
      val dsl = Http4sDsl[F]
      import dsl.*
      HttpRoutes.of[F] {
        case GET -> Root / "health" => Ok("looking good")
      }
    }

    def allRoutes[F[_]: Monad]: HttpRoutes[F] = courseRoutes[F] <+> healthEndpoint[F]


    override def run = EmberServerBuilder.default[IO]
      .default[@@]
      .withHttpApp(allRoutes[IO].orNotFound)
      .build
      .useForever
}

```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2511)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:96)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.loop$3(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:282)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:388)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner