package tools.infrastructure.browser.builder

import java.io.File
import java.net.URL
import java.util

import org.openqa.selenium.safari.{SafariDriver, SafariDriverService, SafariOptions}
import tools.infrastructure.browser.{SafariSession, Session}

class SafariSessionBuilder extends SessionBuilder[SafariOptions] {

  override protected val builder: SafariDriverService.Builder = new SafariDriverService.Builder()

  override protected var executableFile: File = new File("C:\\Web Drivers\\safariDriver.exe")

  override def usingAnyFreePort(): SafariSessionBuilder = {
    portNumber = -1
    this
  }

  override def usingDriverExecutable(file: File): SafariSessionBuilder = {
    executableFile = file
    this
  }

  override def usingPort(port: Int): SafariSessionBuilder = {
    portNumber = port
    this
  }

  override def withEnvironment(environment: util.Map[String, String]): SafariSessionBuilder = {
    builder.withEnvironment(environment)
    this
  }

  override def withLogFile(logFile: File): SafariSessionBuilder = {
    builder.withLogFile(logFile)
    this
  }

  def usingTechnologyPreview(useTechnologyPreview: Boolean): SafariSessionBuilder = {
    builder.usingTechnologyPreview(useTechnologyPreview)
    this
  }

  override protected def getDefaultCapabilities: SafariOptions = new SafariOptions()

  def startLocalSession(mutableCapabilities: SafariOptions): SafariSession = {
    if(portNumber == -1) builder.usingAnyFreePort() else builder.usingPort(portNumber)
    builder.usingDriverExecutable(executableFile)
    val driverService = builder.build()
    driverService.start()
    SafariSession(new SafariDriver(driverService, mutableCapabilities), driverService)
  }

  override def startRemoteSession(url: URL, mutableCapabilities: SafariOptions): SafariSession = ???
}
