package com.ossync.foundations

import cats._
import cats.implicits._
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.circe.*
import cats.effect.{IO, IOApp}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, QueryParamDecoder}
import org.http4s.dsl.impl.{OptionalValidatingQueryParamDecoderMatcher, QueryParamDecoderMatcher}
import org.http4s.ember.server.EmberServerBuilder


import scala.collection.mutable.*
import java.util.UUID
import scala.::
import scala.collection.mutable

object Http4s extends IOApp.Simple {

  // simulate an HTTP server with students and courses.
  type Student = String

  case class Instructor(firstname: String, lastname: String) {
    def getName = {
      s"$firstname $lastname"
    }
  }

  val testInstructor: Instructor = Instructor("Jimmy", "Howard")
  val instructorName: String = testInstructor.getName

  case class Course(id: String, title: String, year: Int, students: List[Student], instructorName: String)
  case class CourseResponse(status: String, courses: List[Course])


  object CourseRepository {
    private val catsEffectCourse = Course(
      "e74ec368-ad7f-4468-a601-93498b029fb8",
      "Cats Effect Course",
      2023,
      List(),
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

  object InstructorQueryParamMatcher extends QueryParamDecoderMatcher[String]("instructor")

  object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Int]("year")

  def courseRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "courses" :? InstructorQueryParamMatcher(instructor) +& YearQueryParamMatcher(maybeYear) =>
        // log something
        val courses = CourseRepository.findCourseByInstructorV2(instructor)
        courses.isEmpty match {
          case true => {
            println(courses.isEmpty)
            Ok(CourseResponse("error: instructor name invalid", courses).asJson)
          }
          case _ =>
        }
        maybeYear match {
          case Some(y) => y.fold(
            _ => BadRequest("Year is invalid"),
            year => {
              val c = courses.filter(_.year == year)
              if (c.isEmpty) Ok(CourseResponse("error", c).asJson)
              else Ok(CourseResponse("success", c).asJson)
            }
          )
          case None => {
            if (courses.isEmpty) Ok(CourseResponse("error", courses).asJson)
            else Ok(CourseResponse("success", courses).asJson)
          }
        }

    }
  }

  override def run = EmberServerBuilder
    .default[IO]
    .withHttpApp(courseRoutes[IO].orNotFound)
    .build
    .use(_ => IO.println("Server Ready") *> IO.never)
}




