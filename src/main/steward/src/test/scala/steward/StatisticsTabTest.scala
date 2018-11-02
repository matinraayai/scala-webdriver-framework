package steward

import tools.infrastructure.browser.Session
import tools.infrastructure.enums.View
import org.testng.Assert
import org.testng.annotations.{AfterTest, BeforeTest, Test}
import steward.page.{Login, Statistics, Topics}

class StatisticsTabTest {
  var session: Session = _
  var statisticsPage: Statistics = _


  @BeforeTest
  def setup(): Unit = {
    session = Session.startLocalSession()
    session.navigate(View.DSA)
    val login = new Login(session)
    login.login("dave", "demouser")
    val topics =  new Topics(session)
    topics.tabSwitcher.switchToStatisticsTab()
    statisticsPage = new Statistics(session)
  }

  @Test
  def test1(): Unit = {
    statisticsPage.setStartDateTo(2016, 10, 16)
    statisticsPage.applyDateRange()
    Assert.assertEquals(statisticsPage.getNumberOfApprovedTopics, 102)
  }

  @Test
  def test2(): Unit = {
    val output = statisticsPage.getQueryCountsByUser
    Assert.assertEquals(output.find(i => i._1 == "mardakani").get._2, 231)
  }


  @AfterTest
  def tearDown(): Unit = {
    session.quit()
  }

}
