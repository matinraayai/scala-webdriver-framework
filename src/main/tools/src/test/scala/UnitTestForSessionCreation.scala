import tools.infrastructure.browser.Session
import tools.infrastructure.enums.{Browser, Server, View}
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.BufferedReader
import java.io.InputStreamReader

class UnitTestForSessionCreation extends BaseSpecTest {

  "A Session" must "be able to create a local instance of the Internet Explorer." in {
    val internetExplorerSession = Session.ie().startLocalSession()
    try {
      var line: String = null
      val p = Runtime.getRuntime.exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe")
      val input = new BufferedReader(new InputStreamReader(p.getInputStream))
      line = input.readLine()
      while (line != null){
        println(line) //<-- Parse data here.
        line = input.readLine()

      }
      input.close()
    } catch {
      case err: Exception =>
        err.printStackTrace()
    }
  }

  it must "be able to close that instance without leaving any process trace" in {

  }
}
