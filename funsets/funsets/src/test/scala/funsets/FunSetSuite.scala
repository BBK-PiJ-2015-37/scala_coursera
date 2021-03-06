package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(2)
    val s5 = singletonSet(-4)
    val s6 = singletonSet(999)
    val s7 = singletonSet(421)
    val s8 = singletonSet(-876)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSets contain correct input") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "s1 did not contain 1")
      assert(contains(s2, 2), "s2 did not contain 2")
      assert(contains(s3, 3), "s3 did not contain 3")
      assert(!contains(s1, 2), "s1 contained unexpected 2")
      assert(!contains(s2, -2), "s2 contained unexpected -2")
      assert(!contains(s3, 75), "s3 contained unexpected 75")

    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains elements common to composing sets") {
    new TestSets {
      val i1 = intersect(s2, s3)
      val i2 = intersect(s2, s4)
      assert(!contains(i1, 2), "Intersect of 2 & 3 contains unexpected 2")
      assert(!contains(i1, 3), "Intersect of 2 & 3 contains unexpected 3")
      assert(contains(i2, 2), "Intersect of 2 & 4 does not contain 2")
    }
  }

  test("diff contains elements not in both composing sets") {
    new TestSets {
      val d1 = diff(s2, s3)
      val d2 = diff(s2, s4)
      assert(contains(d1, 2), "Diff of 2 & 3 does not contain 2")
      assert(!contains(d1, 3), "Diff of 2 & 3 unexpectedly contains 3")
      assert(!contains(d2, 2), "Diff of 2 & 4 unexpectedly contains 2")
    }
  }

  test("filter correctly identifies elements of set that satisfy predicate") {
    new TestSets {
      val f1 = union(s1, s2)
      val f2 = union(s3, s5)
      val f3 = union(f1, f2)
      val filter1 = filter(f3, x => x % 2 == 0)
      val filter2 = filter(f3, x => x < 4)
      assert(contains(filter1, 2), "Set of even numbers does not contain 2")
      assert(contains(filter1, -4), "Set of even numbers does not contain 4")
      assert(!contains(filter1, 1), "Set of even numbers unexpectedly contains 1")
      assert(!contains(filter2, 4), "Set of numbers less than 4 unexpectedly contains 4")
      assert(contains(filter2, 1), "Set of numbers less than 4 does not contain 1")
    }
  }

  test("forall functions correctly checks all values from -1000 to 1000 against a predicate") {
    new TestSets {
      val f1 = union(s1, s3) // 1,3
      val f2 = union(s2, s4) // 2,2
      val f3 = union(s5, s8) // -4, -876
      val f4 = union(s6, s7) // 999, 421
      val f5 = union(f1, f4) // all odd
      val f6 = union(f2, f3) // all even
      assert(forall(f6, x => x % 2 == 0), "Not all elements in f6 are even")
      assert(!forall(f5, x => x % 2 == 0), "Not all elements in f5 are odd")
      assert(forall(f3, x => x < 0), "Not all elements in f3 are less than zero")
    }
  }

  test("exist function") {
    new TestSets {
      val f1 = union(s1, s3) // 1,3
      val f2 = union(s2, s4) // 2,2
      val f3 = union(s5, s8) // -4, -876
      val f4 = union(s6, s7) // 999, 421
      val f5 = union(f1, f4) // all odd
      val f6 = union(f2, f3) // all even
      assert(exists(f6, x => x == 2), "No 2 found in f6")
      assert(exists(f4, x => x % 3 == 0), "f4 does not contain a multiple of 3")
    }
  }

  test("map function") {
    new TestSets {
      val f1 = union(s1, s3) // 1,3
      val f2 = union(s2, s4) // 2,2
      val f3 = union(s5, s8) // -4, -876
      val f4 = union(s6, s7) // 999, 421
      val f5 = union(f1, f4) // all odd
      val f6 = union(f2, f3) // all even
      assert(contains(map(f6, x => x / 2), -438))
      assert(contains(map(f4, x => x + 1), 1000))
    }
  }


}
