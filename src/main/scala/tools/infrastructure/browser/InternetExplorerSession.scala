package tools.infrastructure.browser

import org.openqa.selenium.ie.{InternetExplorerDriver, InternetExplorerDriverService}

case class InternetExplorerSession(protected val parentRemoteWebDriver: InternetExplorerDriver,
                                   protected val remoteWebDriverService: InternetExplorerDriverService) extends Session {}