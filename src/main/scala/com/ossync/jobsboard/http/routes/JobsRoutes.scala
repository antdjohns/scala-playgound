package com.ossync.jobsboard.http.routes

import cats.*
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.Status
import org.http4s.dsl.impl.Responses.OkOps
import org.http4s.dsl.impl.*
import cats.implicits.*
import cats.effect.*

import scala.collection.mutable.*

import com.ossync.jobsboard.domain.job.*
import com.ossync.jobsboard.http.responses.* 

import org.http4s.FormDataDecoder.formEntityDecoder
import org.http4s.circe.CirceSensitiveDataEntityDecoder.circeEntityDecoder

import io.circe.generic.auto.*

import org.http4s.circe.CirceEntityCodec.* 
import java.util.UUID
import cats.MonadThrow

class JobsRoutes[F[_]: Concurrent] extends Http4sDsl[F] {
    private val database = Map[String, Job]()

    private val allJobRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        case GET -> Root => Ok(database.values)
    }

    private val findJobRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
        case GET -> Root / uuid => uuid match {
            case uuid => 
                database.get(uuid) match {
                case Some(value) => Ok(value) // Create Response Objects
                case None => NotFound(FailureResponse("not found"))
            }
        }
    }

    def createJob(jobInfo: JobInfo): F[Job] = {
       Job(
        id = UUID.randomUUID(), 
        date = System.currentTimeMillis(), 
        ownerEmail = "", 
        jobInfo = JobInfo(), 
        false
        ).pure[F]
    }

    
    private val createJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
        case req @ POST -> Root / "create" => 
            for {
                jobInfo <- req.as[JobInfo]
                job <- createJob(jobInfo)
                resp <- Created(job)
            } yield resp 
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

object JobsRoutes {
    def apply[F[_]: Concurrent] = new JobsRoutes[F] 
}
