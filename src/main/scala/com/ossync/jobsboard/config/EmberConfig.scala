package com.ossync.jobsboard.config

import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*
import com.comcast.ip4s.*
import pureconfig.error.CannotConvert

final case class EmberConfig(host: String, port: Int) derives ConfigReader

// given configReader: ConfigReader[EmberConfig] = deriveReader[EmberConfig]

object EmberConfig {
    // need given ConfigReader[Host] + given ConfigReader[Port] => compiler generates ConfigReader[EmberConfig]
    given hostReader: ConfigReader[Host] = ConfigReader[String].emap { hostString => 
        Host
        .fromString(hostString)
        .toRight(CannotConvert(hostString, "Host", "invalid host"))
    }

    given portReader: ConfigReader[Port] = ConfigReader[Int].emap { portInt => 
        Port
        .fromInt(portInt) 
        .toRight(CannotConvert(portInt.toString, "Port", "invalid port"))
    }
}