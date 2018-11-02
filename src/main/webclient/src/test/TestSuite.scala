import org.testng.annotations.{AfterTest, BeforeTest}
import tools.infrastructure.browser.Session
import tools.infrastructure.enums._

/**
  * A base trait for all the test classes to inherit from.
  */
trait TestSuite {

  val browser: Browser.Value

  val session: Session

  def navigateToView(view: View.Value, server: Server.Value): Session = {
    session.navigate().to("https://shrine-" + server.toString + ".catalyst:6443/" +
      view.toString)
    session
  }

  @BeforeTest
  def setup(): Unit



  @AfterTest
  def tearDown(): Unit = {
    session.quit()
  }

}
