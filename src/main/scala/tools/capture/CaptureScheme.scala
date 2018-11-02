package tools.capture

import tools.infrastructure.enums.How.How


trait CaptureScheme {

  def waitForSingle(how: How, locator: String): SingleLocatingStep

  def waitForMultiple(how: How, locator: String): MultipleLocatingStep

  def captureSingle(how: How, locator: String): OptionalLocatingStep

  def captureMultiple(how: How, locator: String): MultipleLocatingStep

}
