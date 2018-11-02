package tools.infrastructure.browser.builder

import java.io.File
import java.net.URL
import java.util

import org.openqa.selenium.firefox._
import tools.infrastructure.browser.{FireFoxSession, Session}

class FireFoxSessionBuilder extends SessionBuilder[FirefoxOptions] {

  protected val builder: GeckoDriverService.Builder = new GeckoDriverService.Builder()

  protected var executableFile: File = new File("C:\\Web Drivers\\geckodriver.exe")

  override def usingAnyFreePort(): FireFoxSessionBuilder = {
    portNumber = -1
    this
  }

  override def usingDriverExecutable(file: File): FireFoxSessionBuilder = {
    executableFile = file
    this
  }

  override def usingPort(port: Int): FireFoxSessionBuilder = {
    portNumber = port
    this
  }

  override def withEnvironment(environment: util.Map[String, String]): FireFoxSessionBuilder = {
    builder.withEnvironment(environment)
    this
  }

  override def withLogFile(logFile: File): FireFoxSessionBuilder = {
    builder.withLogFile(logFile)
    this
  }

  def usingFireFoxBinary(firefoxBinary: FirefoxBinary): FireFoxSessionBuilder = {
    builder.usingFirefoxBinary(firefoxBinary)
    this
  }

  override protected def getDefaultCapabilities: FirefoxOptions =
    new FirefoxOptions().setAcceptInsecureCerts(true)

  def startLocalSession(mutableCapabilities: FirefoxOptions): FireFoxSession = {
    if(portNumber == -1) builder.usingAnyFreePort() else builder.usingPort(portNumber)
    builder.usingDriverExecutable(executableFile)
    val driverService = builder.build()
    driverService.start()
    FireFoxSession(new FirefoxDriver(driverService, mutableCapabilities), driverService)
  }

  override def startRemoteSession(url: URL, mutableCapabilities: FirefoxOptions) = ???
}
