package tools.infrastructure.enums

/**
  * Represents the tool options for testing SHRINE.
  */
object View extends Enumeration {
  val DSA = Value("steward")
  val DASHBOARD = Value("shrine-dashboard")
  val WEBCLIENT = Value("shrine-webclient")
}