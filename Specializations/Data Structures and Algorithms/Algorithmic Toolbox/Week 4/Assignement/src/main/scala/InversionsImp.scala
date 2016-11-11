import java.io._
import java.util.StringTokenizer

import scala.annotation.tailrec

object InversionsImp {

  def inv(list : List[Int]) : Long = doInv(list)._1

  def doInv(list : List[Int]) : (Long, List[Int]) =
    if (list.length <= 1) {
      (0, list)
    } else {
      val (left, right) = list.splitAt(list.length / 2)
      val (leftCount, leftList) = doInv(left)
      val (rightCount, rightList) = doInv(right)
      val (mergeCount, mergeList) = doMerge(leftList, rightList)
      (leftCount + rightCount + mergeCount, mergeList)
    }

  def doMerge(left : List[Int], right : List[Int], count : Long = 0) : (Long, List[Int]) =
    (left, right) match {
      case (Nil, r) => (count, r)
      case (l, Nil) => (count, l)
      case (lhead :: ltail, rhead :: rtail) =>
        if (lhead <= rhead) {
          val (lcount, list) = doMerge(ltail, right, count)
          (count + lcount, lhead :: list)
        } else {
          val (rcount, list) = doMerge(left, rtail, count)
          (count + left.length + rcount, rhead :: list)
        }
    }

    def msort(xs: List[Int], totalCount: Long = 0): (Long, List[Int]) = {
      def merge(left: List[Int], right: List[Int], count : Long = 0): (Long, Stream[Int]) = (left, right) match {
        case (x :: xs, y :: ys) if x <= y => {
          val (c, m) = merge(xs, right, count)
          (count + c, Stream.cons(x, m))
        }
        case (x :: xs, y :: ys) if x > y => {
          val (c, m) = merge(left, ys, count)
          (c + count + left.length, Stream.cons(y, m))
        }
        case _ => if (left.isEmpty) (count, right.toStream) else (count, left.toStream)
      }
      val n = xs.length / 2
      if (n == 0) (totalCount, xs)
      else {
        val (ys, zs) = xs splitAt n
        val (countL, mergedL) = msort(ys)
        val (countR, mergedR) = msort(zs)
        val (countMerged, merged) = merge(mergedL, mergedR)
        ( countMerged + countL + countR, merged.toList)
      }
    }

  def main(args: Array[String]): Unit = {

    new Thread(null, new Runnable() {
      def run() {
        try
          runInversions()

        catch {
          case e: IOException => {
          }
        }
      }
    }, "1", 1 << 26).start()
  }

  def runInversions() = {
    val scanner: FastScanner = new FastScanner(System.in)
    val n: Int = scanner.nextInt
    //val a: List[Int] = List[Int]()
    var i: Int = 0

    /*while (i < n) {
      scanner.nextInt :: a
      i = i + 1
    }*/

    def buildList(n: Int, l: List[Int]): List[Int] =
      if (n == 0) l
      else scanner.nextInt :: buildList(n - 1, l)

    val a = buildList(n, Nil)

    println(msort(a)._1)
  }

  class FastScanner(val stream: InputStream) {

    var br: BufferedReader = null
    var st: StringTokenizer = null

    try
      br = new BufferedReader(new InputStreamReader(stream))

    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }

    def next: String = {
      while (st == null || !st.hasMoreTokens)
        try
          st = new StringTokenizer(br.readLine)

        catch {
          case e: IOException => {
            e.printStackTrace()
          }
        }
      st.nextToken
    }

    def nextInt: Int = next.toInt
  }
}
