package tools.infrastructure.wait

import java.util.concurrent.TimeUnit

/**
  * A Factory for [[tools.infrastructure.wait.ConditionWaiter]].
  */
object Wait {

  def withTimeOut(duration: Long, timeUnit: TimeUnit): ConditionWaiter = {
    val waiter = new ConditionWaiter()
    waiter.withTimeOut(duration, timeUnit)
    waiter
  }

  def withPollingTime(duration: Long, timeUnit: TimeUnit): ConditionWaiter = {
    val waiter = new ConditionWaiter()
    waiter.withPollingTime(duration, timeUnit)
    waiter
  }

  def withErrorMessage(message: String): ConditionWaiter = {
    val waiter = new ConditionWaiter()
    waiter.withErrorMessage(message)
    waiter
  }

  def until[T](condition: (() => T)): T = {
    val waiter = new ConditionWaiter()
    waiter.until(condition)
  }

  def untilOneBecomesTrue(conditions: (() => Any)*): (Int, Any) = {
    val waiter = new ConditionWaiter()
    waiter.untilOneReturnsValidResult(conditions: _*)
  }

}