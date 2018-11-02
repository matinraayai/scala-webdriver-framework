package tools.infrastructure.browser.builder
import java.io.File
import java.net.URL
import java.util

import org.openqa.selenium.ie._
import tools.infrastructure.browser.{InternetExplorerSession, Session}

class IESessionBuilder extends SessionBuilder[InternetExplorerOptions] {

  override protected val builder: InternetExplorerDriverService.Builder =
    new InternetExplorerDriverService.Builder()

  override protected var executableFile: File = new File("C:\\Web Drivers\\IEDriverServer.exe")

  def usingAnyFreePort(): IESessionBuilder = {
    portNumber = -1
    this
  }

  def usingDriverExecutable(file: File): IESessionBuilder = {
    executableFile = file
    this
  }

  def usingPort(port: Int): IESessionBuilder = {
    portNumber = port
    this
  }

  def withEnvironment(environment: util.Map[String, String]): IESessionBuilder = {
    builder.withEnvironment(environment)
    this
  }

  def withLogFile(logFile: File): IESessionBuilder = {
    builder.withLogFile(logFile)
    this
  }

  def withEngineImplementation(engineImplementation: InternetExplorerDriverEngine): IESessionBuilder = {
    builder.withEngineImplementation(engineImplementation)
    this
  }

  def withHost(host: String): IESessionBuilder = {
    builder.withHost(host)
    this
  }

  def withLogLevel(logLevel: InternetExplorerDriverLogLevel): IESessionBuilder = {
    builder.withLogLevel(logLevel)
    this
  }

  def withSilent(silent: Boolean): IESessionBuilder = {
    builder.withSilent(silent)
    this
  }

  protected def getDefaultCapabilities: InternetExplorerOptions = new InternetExplorerOptions()

  def startLocalSession(mutableCapabilities: InternetExplorerOptions = getDefaultCapabilities):
  InternetExplorerSession = {
    if(portNumber == -1) builder.usingAnyFreePort() else builder.usingPort(portNumber)
    builder.usingDriverExecutable(executableFile)
    val driverService = builder.build()
    driverService.start()
    InternetExplorerSession(new InternetExplorerDriver(driverService, mutableCapabilities), driverService)
  }

  def startRemoteSession(url: URL, mutableCapabilities: InternetExplorerOptions):
  InternetExplorerSession = ???
}
