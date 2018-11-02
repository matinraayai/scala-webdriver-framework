package tools.infrastructure.browser

import org.openqa.selenium.safari.{SafariDriver, SafariDriverService}

case class SafariSession(protected val parentRemoteWebDriver: SafariDriver,
                         protected val remoteWebDriverService: SafariDriverService)
  extends Session {}
