import tools.infrastructure.enums.{Server, View}
import dashboard.{DashboardBasePage, DashboardI2B2Connections, DashboardSummary}
import tools.infrastructure.browser.Session
import org.testng.annotations.{AfterTest, BeforeTest, Test}
import steward.enums.{NavigationTab, TopicStatus}
import steward.page.Login

class testForDashboard {
  var session: Session = _
  var loginPage: Login = _
  @BeforeTest
  def setup(): Unit = {
    Session.setServer(Server.DEV_ONE)
    session = Session.startLocalSession()
  }


  @Test
  def test1(): Unit = {
    session.navigate(View.DASHBOARD)
    loginPage = new Login(session)
    loginPage.login("dave","demouser")
    val dashboard = new DashboardSummary(session)
    dashboard.tabSwitcher.switchToI2B2ConnectionsTab()
  }

  @AfterTest
  def teardown(): Unit = {
    session.quit()
  }
}
