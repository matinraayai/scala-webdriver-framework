package tools.infrastructure.browser.builder

import java.io.File
import java.net.URL
import java.util

import org.openqa.selenium.MutableCapabilities
import org.openqa.selenium.remote.service.DriverService
import tools.infrastructure.browser.Session


trait SessionBuilder[MutableCapabilitiesType <: MutableCapabilities] {

  protected val builder: DriverService.Builder[_, _]

  protected var executableFile: File

  protected var portNumber: Int = -1

  protected val defaultNodeURL: URL = new URL("http://localhost:4444/wd/hub")

  def usingAnyFreePort(): SessionBuilder[MutableCapabilitiesType]

  def usingDriverExecutable(file: File): SessionBuilder[MutableCapabilitiesType]

  def usingPort(port: Int): SessionBuilder[MutableCapabilitiesType]

  def withEnvironment(environment: util.Map[String, String]): SessionBuilder[MutableCapabilitiesType]

  def withLogFile(logFile: File): SessionBuilder[MutableCapabilitiesType]

  protected def getDefaultCapabilities: MutableCapabilitiesType

  def startLocalSession(mutableCapabilities: MutableCapabilitiesType = getDefaultCapabilities): Session

  def startRemoteSession(url: URL, mutableCapabilities: MutableCapabilitiesType): Session

}
