import java.io.File
import java.time.Duration

import tools.infrastructure.browser.Session
import tools.infrastructure.enums.{Browser, _}
import org.testng.Assert
import org.testng.annotations.{AfterTest, BeforeTest, Test}
import webclient.page.{Eula, Home, Login}
import webclient.section.component.TreeItem


class TreeItemTest extends TestSuite {
  override val browser: Browser.Value = Browser.CHROME
  val session: Session = Session.chrome().startLocalSession()
  navigateToView(View.WEBCLIENT, Server.QA_ONE)

  var home: Home = _
  var tree: TreeItem = _

  @BeforeTest
  def setup(): Unit = {
    val loginPage = Login(session)
    loginPage.login("shrine", "demouser")
    val eula = Eula(session)
    eula.agree()
    home = Home(session)
    tree = home.navigate_FindTermsBottomTab.navigateTermsBottomTab.getTreeOfOntology
  }

  @Test
  def test1(): Unit = {
    Assert.assertEquals(tree.getText, "SHRINE Ontology")
    val children = tree.expandAndGetChildren
    val children1 = children.head.expandAndGetChildren
    val children2 = children1.head.expandAndGetChildren
    Assert.assertEquals(children2.head.getText, "0-9 years old")
    home.dragTreeItemToQueryTool(children2.head, 0)
    home.queryToolBottomTab.selectTopicByIndex(1)
    home.queryToolBottomTab.pressRun()
    val runDialogueBox = home.getRunQueryDialogueBox
    runDialogueBox.setQueryName("Haha, you thought you won't be automated!!!")
    runDialogueBox.toggleAgeBreakdown()
    runDialogueBox.toggleGenderBreakdown()
    runDialogueBox.toggleRaceBreakdown()
    runDialogueBox.toggleVitalBreakdown()
    runDialogueBox.pressRun()
    session.pause(Duration.ofSeconds(5)).perform()
    val previousQueriesItems = home.previousQueriesBottomTab.getListOfItems
    home.dragTreeItemToQueryTool(previousQueriesItems(8), 0)
    session.pause(Duration.ofSeconds(5)).perform()
    Assert.assertEquals(previousQueriesItems.head.getText, "Haha, you thought you won't be automated!!! [12-6-2017] [shrine]")
  }


}
