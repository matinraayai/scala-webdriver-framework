package tools.infrastructure.browser.builder

import java.io.File
import java.net.URL
import java.util

import org.openqa.selenium.chrome.{ChromeDriver, ChromeDriverService, ChromeOptions}
import tools.infrastructure.browser.{ChromeSession, Session}

class ChromeSessionBuilder extends SessionBuilder[ChromeOptions] {

  override protected val builder:
    ChromeDriverService.Builder = new ChromeDriverService.Builder()

  override protected var executableFile: File = new File("C:\\Web Drivers\\chromedriver.exe")

  def usingAnyFreePort(): ChromeSessionBuilder = {
    portNumber = -1
    this
  }

  def usingDriverExecutable(file: File): ChromeSessionBuilder = {
    executableFile = file
    this
  }

  def usingPort(port: Int): ChromeSessionBuilder = {
    portNumber = port
    this
  }

  def withEnvironment(environment: util.Map[String, String]): ChromeSessionBuilder = {
    builder.withEnvironment(environment)
    this
  }

  def withLogFile(logFile: File): ChromeSessionBuilder = {
    builder.withLogFile(logFile)
    this
  }

  def withVerbose(verbose: Boolean): ChromeSessionBuilder = {
    builder.withVerbose(verbose)
    this
  }

  def withSilent(silent: Boolean): ChromeSessionBuilder = {
    builder.withSilent(silent)
    this
  }

  def withWhitelistedIps(whitelistedIps: String): ChromeSessionBuilder = {
    builder.withWhitelistedIps(whitelistedIps)
    this
  }

  protected def getDefaultCapabilities: ChromeOptions = new ChromeOptions()

  def startLocalSession(mutableCapabilities: ChromeOptions): ChromeSession = {
    if(portNumber == -1) builder.usingAnyFreePort() else builder.usingPort(portNumber)
    builder.usingDriverExecutable(executableFile)
    val driverService = builder.build()
    driverService.start()
    ChromeSession(new ChromeDriver(driverService, mutableCapabilities), driverService)
  }

  override def startRemoteSession(url: URL, mutableCapabilities: ChromeOptions): ChromeSession = ???
}

