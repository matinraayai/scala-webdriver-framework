package tools.infrastructure.browser

object JavascriptCode {
  final val SCROLL_TO_BEGINNING_OF_DOCUMENT: String =
    "window.scrollTo(0,-document.body.scrollHeight);"
  final val SCROLL_TO_END_OF_DOCUMENT: String =
    "window.scrollTo(0,document.body.scrollHeight);"

}
