package steward

import tools.infrastructure.browser.Session
import tools.infrastructure.enums.View
import org.scalatest.Assertions
import org.testng.Assert
import org.testng.annotations._
import org.testng.asserts.SoftAssert
import steward.page.{Login, Topics}


class TopicsTabTest {

  var session: Session = _
  var topicsTab: Topics = _

  @BeforeTest
  def setup(): Unit = {
    session = Session.startLocalSession()
    session.navigate(View.DSA)
    val login: Login = new Login(session)
    login.login("dave", "demouser")
    topicsTab = new Topics(session)
  }

  @Test
  def test1_checkInitialPageStatus(): Unit = {
    Assert.assertEquals(topicsTab.tabSwitcher.getNameOfCurrentlySelectedTab, "Topics")
    Assert.assertEquals(topicsTab.getCurrentlyShownTopicStatus, "PENDING")
    Assert.assertEquals(topicsTab.getSortStatus , ("Last Updated", "Ascending"))
    Assert.assertEquals(topicsTab.getCurrentTableContents.length, 20)
    Assert.assertEquals(topicsTab.getTotalNumberOfPagesFromPaginationStatus, 14)
  }
  @Test
  def test2_checkSortByID(): Unit = checkSort("ID")

  @Test
  def test3_checkSortByTopic(): Unit = checkSort("Topic Name")

  @Test
  def test4_checkSortByUsername(): Unit = checkSort("Username")

  @Test
  def test5_checkSortByDateCreated(): Unit = checkSort("Date Created")

  @Test
  def test6_checkSortByLastUpdated(): Unit = checkSort("Last Updated")

  @Test
  def test7_changeTopicStatusToApproved(): Unit = {
    topicsTab.switchTopicStatus("APPROVED")
    Assert.assertEquals(topicsTab.getCurrentlyShownTopicStatus, "APPROVED")
  }

  @Test
  def test8_changeTopicStatusToRejected(): Unit = {
    topicsTab.switchTopicStatus("REJECTED")
    Assert.assertEquals(topicsTab.getCurrentlyShownTopicStatus, "REJECTED")
  }

  @Test
  def test9_traverseWholeTableContents(): Unit = {
    var i: Int = 0
    val lastPage: Int = topicsTab.getTotalNumberOfPagesFromPaginationStatus
    while(i != lastPage) {
      //topicsTab.goToNextPage()
      test2_checkSortByID()
      test3_checkSortByTopic()
      test4_checkSortByUsername()
      test5_checkSortByDateCreated()
      i = i + 1
    }
  }

  @AfterTest
  def tearDown(): Unit = {
    session.quit()
  }

  def checkSort(sortBy: String): Unit = {
    topicsTab.changeSortStatus(sortBy)
    Assert.assertEquals(topicsTab.getSortStatus, (sortBy, "Ascending"))
    Assert.assertEquals(topicsTab.getTableBodyRowCount, 20)
    topicsTab.changeSortStatus(sortBy)
    Assert.assertEquals(topicsTab.getTableBodyRowCount, 20)
    assert(topicsTab.getSortStatus == (sortBy, "Descending"))
  }



}
