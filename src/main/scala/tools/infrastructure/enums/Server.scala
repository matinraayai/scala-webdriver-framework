package tools.infrastructure.enums

/**
  * Represents the different servers used with this framework.
  */
object Server extends Enumeration {
  val DEV_ONE = Value("dev1")
  val DEV_TWO = Value("dev2")
  val QA_ONE = Value("qa1")
  val QA_TWO = Value("qa2")
  val QA_THREE = Value("qa3")
}