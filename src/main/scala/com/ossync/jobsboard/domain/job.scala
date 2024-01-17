package com.ossync.jobsboard.domain

import java.util.UUID
import scala.Option

object job {
  case class Job(
    id: UUID,
    date: Long,
    ownerEmail: String,
    jobInfo: JobInfo,
    active: Boolean = false
    )

  case class JobInfo(
    company: String = "",
    title: String = "",
    remote: Boolean = false,
    location: String = "",
    description: String = "",
    extenralUrl: String = "",

    salaryLo: Option[Int] = None,
    salaryHi: Option[Int] = None,
    currency: Option[String] = None,
    country: Option[String] = None,
    tags: Option[List[String]] = None,
    image: Option[String] = None,
    seniority: Option[String] = None,
    other: Option[String] = None
  )

  object JobInfo {
    val empty: JobInfo = JobInfo()
  }
}
