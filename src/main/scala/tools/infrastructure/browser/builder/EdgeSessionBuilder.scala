package tools.infrastructure.browser.builder
import java.io.File
import java.net.URL
import java.util

import org.openqa.selenium.edge.{EdgeDriver, EdgeDriverService, EdgeOptions}
import tools.infrastructure.browser.{EdgeSession, Session}

class EdgeSessionBuilder extends SessionBuilder[EdgeOptions] {

  override protected val builder: EdgeDriverService.Builder = new EdgeDriverService.Builder()

  protected var executableFile: File = new File("C:\\Web Drivers\\MicrosoftWebDriver.exe")

  def usingAnyFreePort(): EdgeSessionBuilder = {
    portNumber = -1
    this
  }

  def usingDriverExecutable(file: File): EdgeSessionBuilder = {
    executableFile = file
    this
  }

  def usingPort(port: Int): EdgeSessionBuilder = {
    portNumber = port
    this
  }

  def withEnvironment(environment: util.Map[String, String]): EdgeSessionBuilder = {
    builder.withEnvironment(environment)
    this
  }

  def withLogFile(logFile: File): EdgeSessionBuilder = {
    builder.withLogFile(logFile)
    this
  }

  protected def getDefaultCapabilities: EdgeOptions = new EdgeOptions()

  def startLocalSession(mutableCapabilities: EdgeOptions): EdgeSession = {
    if(portNumber == -1) builder.usingAnyFreePort() else builder.usingPort(portNumber)
    builder.usingDriverExecutable(executableFile)
    val driverService = builder.build()
    driverService.start()
    EdgeSession(new EdgeDriver(driverService, mutableCapabilities), driverService)
  }

  def startRemoteSession(url: URL, mutableCapabilities: EdgeOptions): EdgeSession = ???
}
