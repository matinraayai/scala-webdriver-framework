package tools.infrastructure.browser

import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.service.DriverService

case class FireFoxSession(protected val parentRemoteWebDriver: FirefoxDriver, protected val remoteWebDriverService: DriverService) extends Session {

}
