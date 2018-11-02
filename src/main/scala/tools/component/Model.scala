package tools.component

import tools.infrastructure.element.DOMElement
import tools.capture._
import tools.infrastructure.`trait`.DomElementLocator

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable


/**
  * <p>A Model can briefly be described as a <b><u>context in the DOM with user defined functionality</u></b>.</p>
  * <p>More precisely, a Model is an <b><u>element on the DOM</b></u> that contains a set of methods describing
  * user functionality of its shown context. For example, a [[tools.component.Page]] or a
  *  [[tools.component.Section]] of a page are both Models.
  *
  * @author Matin Raayai Ardakani
  */
trait Model {

  //Typedef for SINGE_ELEMENT_LOCATOR_MAP for readability.
  type SMap = TrieMap[String, SingleLocatingProcedure]

  //Typedef for MULTIPLE_ELEMENT_LOCATOR_MAP for readability.
  type MMap = TrieMap[String, MultipleLocatingPath]

  //Typedef for OPTIONAL_ELEMENT_LOCATOR_MAP for readability.
  type OMap = TrieMap[String, OptionalLocatingPath]

  /*
    * Unlike what is proposed in Selenium Design Pattern Resources, a model never saves instances
    * of DOMElements of interest; Instead, it provides the inheriting class with the means to "capture"
    * an element in a scope for brief interaction.
    * Saving instances of elements is risky. Due to the fact that in a dynamic web application,
    * elements on the DOM are constantly getting updated as the user interacts with the app, using
    * a cached instance of an element leads to too much focus on inner workings of the DOMElement
    * class's implementation (like whether the element's reference on the DOM goes stale after a while).
    * Relocating the elements over and over again avoids complicating the inheriting classes. Furthermore,
    * by relocating an element on every use case, one can make sure that the element meets the minimum
    * requirements of the BA (whether it is on the DOM, whether it is visible, clickable, ...).
    */

  /**
    * @return The domain element of the model as a [[tools.infrastructure.`trait`.DomElementLocator]].
    *         All the elements in the Model are retrieved from the domain element.
    */
  protected def captureModelDomainElement: DomElementLocator

  /*
  As mentioned before, this trait doesn't save instances of elements and relocates elements over and
  over again. This means writing a method for each element of interest. As the Model grows bigger and
  the application scales, organizing and debugging these methods will be a daunting task. Therefore,
  this trait uses a map to keep track of elements of interest and the steps that should be taken in order
  to capture them. The steps can be described using the DSL and library provided in the capture package.
  In order to see how to use them, see the implicit conversions further down or see a concrete implementation
  of this trait.
   */

  /**
    * Holds all the [[tools.capture.SingleLocatingProcedure]] for locating an element of
    * interest.
    */
  protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap()

  /**
    * Holds all the [[tools.capture.MultipleLocatingPath]] for locating elements of
    * interest.
    */
  protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap()

  /**
    * Holds all the [[tools.capture.OptionalLocatingPath]] for locating an element of
    * interest that might or might not exist.
    */
  protected lazy val OPTIONAL_ELEMENT_LOCATOR_MAP: OMap = TrieMap()

  //Implicit conversions, used for creating a DSL for organizing locating procedures from the Maps:

  //1. Conversions used to call element-related methods directly on element names.

  /**
    * Uses the provided elementKey to refer to [[SINGLE_ELEMENT_LOCATOR_MAP]] and runs the
    * [[tools.capture.SingleLocatingProcedure]] value associated with it, if found.
    *
    * @param elementKey key of the element of interest in the [[SINGLE_ELEMENT_LOCATOR_MAP]]
    * @return the desired [[tools.infrastructure.element.DOMElement]]
    * @throws NoSuchElementException if the elementKey is not found in the SMap.
    */
  protected implicit def locateSingleElementUsingMap(elementKey: String): DOMElement = {
    SINGLE_ELEMENT_LOCATOR_MAP.getOrElse(elementKey,
      throw new NoSuchElementException(s"$elementKey was not found in the SMap.")).
      run(captureModelDomainElement)
}

  /**
    * Uses the provided elementKey to refer to [[OPTIONAL_ELEMENT_LOCATOR_MAP]] and runs the
    * [[tools.capture.OptionalLocatingPath]] value associated with it, if found.
    *
    * @param elementKey key of the element of interest in the [[OPTIONAL_ELEMENT_LOCATOR_MAP]]
    * @return an Optional value of [[tools.infrastructure.element.DOMElement]]
    * @throws NoSuchElementException if the elementKey is not found in the OMap.
    */
  protected implicit def locateOptionalElementUsingMap(elementKey: String): Option[DOMElement] = {
    OPTIONAL_ELEMENT_LOCATOR_MAP(elementKey).run(captureModelDomainElement)
  }


  protected implicit class conflictResolvingElementLookUps(elementKey: String) {

    /**
      * Uses the provided elementKey to refer to [[MULTIPLE_ELEMENT_LOCATOR_MAP]] and runs the
      * [[tools.capture.MultipleLocatingPath]] value associated with it, if found.
      *
      * @return the desired instances of [[tools.infrastructure.element.DOMElement]] inside a mutable Buffer.
      * @throws NoSuchElementException if the elementKey is not found in the MMap.
      */
    def <> : mutable.Buffer[DOMElement] = {
      MULTIPLE_ELEMENT_LOCATOR_MAP.getOrElse(elementKey,
        throw new NoSuchElementException(s"$elementKey was not found in the MMap.")).
        run(captureModelDomainElement)
    }

    def >> : DOMElement = locateSingleElementUsingMap(elementKey)
  }

  //2. Conversions used to convert special cases into a whole procedure.

  protected implicit def convertToProcedure(procedure: SingleLocatingStep): SingleLocatingProcedure = {
    SingleLocatingProcedure(procedure)
  }

  protected implicit def convertToProcedure(procedure: MultipleLocatingStep): MultipleLocatingPath = {
    MultipleLocatingPath(None, procedure)
  }

  protected implicit def convertToProcedure(procedure: OptionalLocatingStep): OptionalLocatingPath = {
    OptionalLocatingPath(None, procedure)
  }



  //3. Conversion used to extract a single Element procedure from the SMap in order to be used in
  //either MMap or OMap.

  protected implicit def searchProcedure(locatorName: String): SingleLocatingProcedure = {
    SINGLE_ELEMENT_LOCATOR_MAP(locatorName)
  }



  /**
    * Gets the session instance inside this trait to wait for the components to load on the DOM.
    *
    * @param names of the components in the SINGLE_ELEMENT_LOCATOR_MAP and
    *              MULTIPLE_ELEMENT_LOCATOR_MAP to be loaded
    */
  protected def waitUntilElementsAreLoaded(names: String*): Unit = {
    names.foreach(i => try {
      if(SINGLE_ELEMENT_LOCATOR_MAP.contains(i))
        locateSingleElementUsingMap(i)
      else if(MULTIPLE_ELEMENT_LOCATOR_MAP.contains(i))
        i.<>
      else throw new IllegalArgumentException(s"the name $i was not found in either the SMap " +
        s"and the MMap.")
    }
      catch {
        case e: RuntimeException =>
          throw e
      }
    )
  }



}
