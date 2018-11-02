package steward.component

import tools.infrastructure.browser.{JavascriptCode, Session}



//Prepare for deletion. It doesn't make sense to cram all the functionality here.
/**
  * Represents an abstraction over the data shown in the `Topics` tab for steward.
  * @param webTableElementName Name of the element stored in the `Topics` page object.
  * @param paginationElementName Name of the pagination element stored ibn the `Topics` page object.
  * @param paginationStatusElementName Name of the pagination status element (the text under the
  *                                    pagination element.
  * @param session the session object responsible for creating this abstraction.
  */
case class TopicsPagedTable(webTableElementName: String, paginationElementName: String,
                       paginationStatusElementName: String, session: Session) {


}
