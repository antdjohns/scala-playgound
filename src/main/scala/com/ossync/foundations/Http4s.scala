package com.ossync.foundations

import cats.*
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.circe.*
import cats.effect.*
import org.http4s.dsl.*
import org.http4s.*
import org.http4s.dsl.impl.*
import org.typelevel.ci.CIString
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router

import scala.collection.mutable.*
import java.util.UUID
import scala.::
import scala.collection.mutable

object Http4s extends IOApp.Simple {

  // simulate an HTTP server with students and courses.
  type Student = String

  case class Instructor(firstname: String, lastname: String):
    val getName: String = s"$firstname $lastname"

  val testInstructor: Instructor = Instructor("Jimmy", "Howard")

  case class Course(id: String, title: String, year: Int, students: List[Student], instructorName: String)
  case class CourseResponse(status: String, courses: List[Course])


  object CourseRepository {
    private val catsEffectCourse = Course(
      "e74ec368-ad7f-4468-a601-93498b029fb8",
      "Cats Effect Course",
      2023,
      List("Anthony", "Jimmy", "James"),
      testInstructor.getName)

    val courses: Map[String, Course] = mutable.Map(catsEffectCourse.id -> catsEffectCourse)

    def findCourseById(courseId: String): List[Course] = {
      courses.values.filter(_.id == courseId).toList
    }

    def findCourseByInstructorV2(name: String): List[Course] = {
      courses.values.filter(_.instructorName == name).toList
    }

    def findCoursesByInstructor(name: String): Option[List[Course]] = {
      def getCourse(m: mutable.Map[String, Course], list: List[Course]): List[Course] = {
        if (m.isEmpty) return list
        if (m.head._2.instructorName == name) getCourse(m.tail, m.head._2 :: list)
        else getCourse(m.tail, list)
      }

      val list = getCourse(courses, List[Course]())
      if (list.isEmpty) None
      else Some(list)
    }


  }
  // Essential Rest Endpoints
  // GET localhost:8080/courses?instructor=Martin%20Odersky&year=2022
  // GET /courses/{id}

  private object InstructorQueryParamMatcher extends QueryParamDecoderMatcher[String]("instructor")
  private object StudentQueryParamMatcher extends QueryParamDecoderMatcher[String]("student")
  private object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Int]("year")


  def courseRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "courses" :? InstructorQueryParamMatcher(instructor) +& YearQueryParamMatcher(maybeYear) =>
        // log something
        val courses = CourseRepository.findCourseByInstructorV2(instructor)
        courses.isEmpty match {
          case true => {
            Ok(CourseResponse("error: instructor name invalid", courses).asJson)
          }
          case _ =>
        }
        maybeYear match {
          case Some(y) => y.fold(
            _ => BadRequest("Year is invalid"),
            year => {
              val c = courses.filter(_.year == year)
              if c.isEmpty then Ok(CourseResponse("error", c).asJson, Header.Raw(CIString("x-ossync-origin"), "xcv"))
              else Ok(CourseResponse("success", c).asJson)
            }
          )
          case None => {
            if courses.isEmpty then Ok(CourseResponse("error", courses).asJson)
            else Ok(CourseResponse("success", courses).asJson)
          }
        }
      case GET -> Root / "courses" / courseId / "students" =>
        val students = CourseRepository.findCourseById(courseId).map(_.students)
        students.isEmpty match {
          case true => Ok("empty")
          case _ => Ok(students.asJson)
        }
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

  def routesWithPathPrefixes = Router(
    "/api" -> courseRoutes[IO],
    "/private" -> healthEndpoint[IO]
  ).orNotFound

  override def run = EmberServerBuilder
    .default[IO]
    .withHttpApp(routesWithPathPrefixes)
    .build
    .use(_ => IO.println("Server Ready") *> IO.never)
}