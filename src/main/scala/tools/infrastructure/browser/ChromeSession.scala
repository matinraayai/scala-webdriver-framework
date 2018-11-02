package tools.infrastructure.browser

import org.openqa.selenium.chrome.{ChromeDriver, ChromeDriverService, ChromeOptions}

case class ChromeSession(protected val parentRemoteWebDriver: ChromeDriver,
                         protected val remoteWebDriverService: ChromeDriverService) extends Session {


}
