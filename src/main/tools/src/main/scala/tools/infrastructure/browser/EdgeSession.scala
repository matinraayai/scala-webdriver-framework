package tools.infrastructure.browser

import org.openqa.selenium.edge.{EdgeDriver, EdgeDriverService}

case class EdgeSession(protected val parentRemoteWebDriver: EdgeDriver,
                       protected val remoteWebDriverService: EdgeDriverService) extends Session {}
