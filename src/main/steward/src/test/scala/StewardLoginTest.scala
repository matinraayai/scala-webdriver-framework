package steward

import tools.infrastructure.browser.Session
import tools.infrastructure.enums.View
import org.testng.annotations.{AfterTest, BeforeTest, Test}
import org.testng.Assert._
import steward.page.Login
class StewardLoginTest {
  var session: Session = _
  var loginPage: Login = _
  @BeforeTest
  def setup(): Unit = {
    session = Session.startLocalSession()
    session.navigate(View.DSA)
    loginPage = Login(session)
  }

  @Test
  def test1_BothEmptyFieldsTest(): Unit = {
    assert(!loginPage.isSubmitButtonEnabled)
    loginPage.pressSubmitButton()
    //assertEquals(loginPage.evaluatePostSubmitEvents(),"Navigated to the page:Shrine Data Steward")
  }

  @Test
  def test2_usernameFilledPasswordEmpty(): Unit = {
    loginPage.setUsername("dave")
    assert(!loginPage.isSubmitButtonEnabled)
    loginPage.pressSubmitButton()
    //assertEquals(loginPage.evaluatePostSubmitEvents(), "Navigated to the page:Shrine Data Steward")
  }

  @Test
  def test3_passwordFilledUsernameEmpty(): Unit = {
    loginPage.setUsername("")
    loginPage.setUsername("demouser")
    assert(!loginPage.isSubmitButtonEnabled)
    loginPage.pressSubmitButton()
    //assert(loginPage.evaluatePostSubmitEvents() == "Navigated to the page:Shrine Data Steward")
  }

  @Test
  def test4_caseSensitiveUsername(): Unit = {
    loginPage.setUsername("DAVE")
    loginPage.setUsername("demouser")
    assert(loginPage.isSubmitButtonEnabled)
    loginPage.pressSubmitButton()
    //assert(loginPage.evaluatePostSubmitEvents() == "Navigated to the page:Shrine Data Steward")
  }

  @Test
  def test5_caseSensitivePassword(): Unit = {
    loginPage.setUsername("dave")
    loginPage.setUsername("DEMOUSER")
    assertTrue(loginPage.isSubmitButtonEnabled)
    loginPage.pressSubmitButton()
    //assert(loginPage.evaluatePostSubmitEvents() == "Navigated to the page:Shrine Data Steward")
  }

  @Test
  def test6_login(): Unit = {
    loginPage.setUsername("dave")
    loginPage.setUsername("demouser")
    assert(loginPage.isSubmitButtonEnabled)
    loginPage.pressSubmitButton()
    //assert(loginPage.evaluatePostSubmitEvents() == "Navigated to the page: Shrine Data Steward")
  }

  @AfterTest
  def teardown(): Unit = {
    session.quit()
  }
}
