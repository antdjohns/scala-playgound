file://<WORKSPACE>/src/main/scala/com/ossync/foundations/Http4s.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
uri: file://<WORKSPACE>/src/main/scala/com/ossync/foundations/Http4s.scala
text:
```scala
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





```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2582)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.isSelfSym(SymDenotations.scala:714)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:160)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.fold$1(Trees.scala:1627)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.apply(Trees.scala:1629)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1660)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1762)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:281)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1668)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1762)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:281)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.fold$1(Trees.scala:1627)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.apply(Trees.scala:1629)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1666)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1762)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:281)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$13(ExtractSemanticDB.scala:221)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:221)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1660)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1762)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:281)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1715)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1762)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:184)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$11(ExtractSemanticDB.scala:207)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:207)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1761)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1719)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1633)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1762)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:181)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$1(ExtractSemanticDB.scala:145)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:145)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:38)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticdbTextDocument$$anonfun$1(ScalaPresentationCompiler.scala:191)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner