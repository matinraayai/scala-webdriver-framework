package tools.infrastructure.wait

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

import org.openqa.selenium
import org.openqa.selenium.{By, TimeoutException, WebDriver, WebDriverException}
import org.openqa.selenium.support.ui._

import scala.collection.mutable

/**
  * Waits for a certain condition to happen.
  * @author Matin Raayai Ardakani
  */
class ConditionWaiter private[wait] {

  private class MockWebDriver extends org.openqa.selenium.WebDriver {

    override def navigate() = ???

    override def getWindowHandle = ???

    override def switchTo() = ???

    override def findElements(by: By) = ???

    override def manage() = ???

    override def getWindowHandles = ???

    override def getTitle = ???

    override def getCurrentUrl = ???

    override def getPageSource = ???

    override def findElement(by: By) = ???

    override def get(url: String) = ???

    override def quit() = ???

    override def close() = ???
  }

  private val webDriverWait: WebDriverWait =
    new WebDriverWait(new MockWebDriver, ConditionWaiter.DEFAULT_SLEEP_TIME_IN_SECONDS)
  webDriverWait.ignoring(classOf[WebDriverException], classOf[NoSuchElementException])

  //These variables are only saved for debugging messages.
  private var timeOut: Duration = new Duration(ConditionWaiter.DEFAULT_SLEEP_TIME_IN_SECONDS, TimeUnit.SECONDS)

  private var interval: Duration = new Duration(500, TimeUnit.MILLISECONDS)

  private var message: Option[String] = None

  /**
    * Sets the time out duration to the given input.
    * @param duration of the desired time.
    * @param timeUnit of the desired time.
    * @return A self reference.
    */
  def withTimeOut(duration: Long, timeUnit: TimeUnit): ConditionWaiter = {
    webDriverWait.withTimeout(duration, timeUnit)
    timeOut = new Duration(duration, timeUnit)
    this
  }

  /**
    * Sets the polling interval to the given input. Notice that nor this method doesn't check whether the given interval
    * is smaller than the timeout duration itself, nor there is a need to check.
    * @param duration of the desired time.
    * @param timeUnit of the desired time.
    * @return A self reference.
    */
  def withPollingTime(duration: Long, timeUnit: TimeUnit): ConditionWaiter = {
    webDriverWait.pollingEvery(duration, timeUnit)
    interval = new Duration(duration, timeUnit)
    this
  }

  /**
    * Sets the error message to the desired input.
    * @param message the desired string message
    * @return A self reference.
    */
  def withErrorMessage(message: String): ConditionWaiter = {
    webDriverWait.withMessage(message)
    this.message = Some(message)
    this
  }

  /**
    * Executes the condition iteratively with the previously specified polling time interval while ignoring the pre-specified
    * exceptions, until it either returns a <u>valid result</u> or <u>reaches time outs</u>. Valid results include non-null references and
    * the Boolean value true.
    * @param condition a [[Function0]] waiting to "happen". For debugging purposes, it is recommended to have the condition's
    *                  description to be returned by its `toString()` method.
    * @tparam T return type of the condition
    * @return The output of the condition
    * @throws TimeoutException if it doesn't get a valid result by the end of the pre-specified timeout.
    */
  def until[T](condition: (() => T)): T = {
    //Injecting the WebDriverWait object with an ExpectedCondition that has the condition as its apply method.
    //In another words, this converts a Scala Function0 to a Java Runnable.
    val expectedCondition = new ExpectedCondition[T] {
      override def apply(f: selenium.WebDriver): T = condition()
    }
    try
      webDriverWait.until(expectedCondition)
    catch {
      case _: TimeoutException =>
        if(message.isEmpty)
          message = Some(s"Condition $condition failed to return a valid result after $timeOut with polling time of $interval.")
        throw new TimeoutException(message.get)
    }
  }

  /**
    * Executes the given conditions sequentially until one of them returns a valid result, similar to the `until()` method.
    * or reaches timeout. The conditions' results should be mutually exclusive or else a race condition will happen.
    * @param conditions A collection of [[Function0]]s waiting to happen. For debugging purposes, override the `toString()`
    *                   method of each condition with a description of what the condition is.
    * @return a [[Tuple2]], with the first element being the condition that has returned result and the second element being
    *         the result.
    */
  def untilOneReturnsValidResult(conditions: (() => Any)*): (Int, Any) = {
    //One might ask why I'm using an "Atomic Reference" to do a simple task.
    //The truth is, I have tried 4 different approaches to solve this problem. Among them were the one you see below
    //(injecting the WebDriverWait with a bunch of function0s wrapped in ExpectedConditions that manipulate two output
    //variables outside their scopes and then pass them inside the Selenium predefined "Or" Condition, having a bigger
    // ExpectedCondition containing smaller ExpectedConditions that reapplied
    //the conditions over and over using the WebDriverWait, implementing the whole WebDriverWait from scratch myself and
    // 2 or more others that I can't remember. In all those scenarios, something strange happened: If I'd put a break point
    // right before or after the condition polling, I would get correct results. If I'd let the program execute without
    // interruption, timeout would occur. Having a hunch that the jdk debugger couldn't refresh those reference contents
    //just in time, I decided to C++ this thing up. The closest thing I had on my hand to pointers were AtomicReferences.
    //After that, the jdk ran this piece of code without any trouble, but the jdk debugger still waited a huge time until
    //returning results (more than 40 seconds).
    //tl;dr: If you're debugging a piece of code that calls this method, be sure to set the timeout time longer than a minute
    //or so for the jdk to update the contents of the reference it is supposed to return.
    val winnerContent: AtomicReference[Any] = new AtomicReference[Any]()
    var winnerNumber: Int = -1
    class RacingCondition(private val conditionIndex: Int, condition: AtomicReference[() => Any])
      extends ExpectedCondition[Any]  {
      def apply(f: WebDriver): Any = {
        winnerContent.set(condition.get()())
        winnerNumber = conditionIndex
        winnerContent.get()
      }
    }

    var i: Int = 0
    val webDriverCompatibleConditions: mutable.Buffer[AtomicReference[RacingCondition]] = mutable.Buffer()
    for (condition: (() => Any) <- conditions) {
      webDriverCompatibleConditions += new AtomicReference(new RacingCondition(i, new AtomicReference(condition)))
      i = i + 1
    }
    implicit class webDriverWaitCap(webDriverWait: WebDriverWait) {
      def enhancedUntil[T](e: => ExpectedCondition[T]): T = {
        webDriverWait.until(e)
      }
    }
    try
      webDriverWait.enhancedUntil(ExpectedConditions.or(webDriverCompatibleConditions.map(_.get): _*))
    catch {
      case _: TimeoutException =>
        if(message.isEmpty)
          message = Some(s"None of the conditions returned valid results in $timeOut with polling every $interval.\nConditions: $conditions")
        throw new TimeoutException(message.get)
    }
    (winnerNumber, winnerContent.get())

  }
}

private object ConditionWaiter {
  private val DEFAULT_SLEEP_TIME_IN_SECONDS: Long = 30
}
