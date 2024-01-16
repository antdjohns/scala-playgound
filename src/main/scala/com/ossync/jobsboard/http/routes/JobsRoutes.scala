package com.ossync.jobsboard.http.routes

import cats.Monad
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.Status
import org.http4s.dsl.impl.Responses.OkOps
import org.http4s.dsl.impl.*
import cats.implicits.*

class JobsRoutes[F[_]: Monad] extends Http4sDsl[F] {
    private val allJobRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        case GET -> Root => Ok("Todo")
    }

    private val findJobRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        case GET -> Root / uuid => uuid match {
            case uuid => Ok(getJobById(uuid))
        }
    }

    private val createJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
        case POST -> Root / "create" => Ok()
    }

    private val updateJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
        case PUT -> Root / uuid => Ok()
    }

    private val deleteJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
        case DELETE -> Root / uuid => Ok(uuid)
    }

    
    val routes = Router(
        "/jobs" -> (allJobRoutes 
        <+> findJobRoutes
        <+> createJobRoute 
        <+> updateJobRoute
        <+> deleteJobRoute)
    )
  
}

def getJobById(uuid: String): String = {
    "success: " + uuid
}

object JobsRoutes {
    def apply[F[_]: Monad] = new JobsRoutes[F] 
}
