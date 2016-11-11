import scala.annotation.tailrec

def inv(list : List[Int]) : Long = doInv(list)._1

def doInv(list : List[Int]) : (Long, List[Int]) =
  if (list.length <= 1) {
    (0, list)
  } else {
    val (left, right) = list.splitAt(list.length / 2)
    val (leftCount, leftList) = doInv(left)
    val (rightCount, rightList) = doInv(right)
    val (mergeCount, mergeList) = merge(leftList, rightList, Nil)
    (leftCount + rightCount + mergeCount, mergeList)
  }

@tailrec
def merge(l: List[Int], r: List[Int], acc: List[Int], count : Long = 0): (Long, List[Int]) = {
  if (l.isEmpty) (count, acc ::: r)
  else if (r.isEmpty) (count, acc ::: l)
  else {
    if (l.head < r.head)
      merge(l.tail, r, acc ::: List(l.head), count)
    else if (l.head > r.head)
      merge(l, r.tail, acc ::: List(r.head), count + l.length)
    else
      merge(l.tail, r, acc ::: List(l.head), count)
  }
}

/*def doMerge(left : List[Int], right : List[Int], count : Long = 0) : (Long, List[Int]) =
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
  }*/

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

val l1 = List(2, 3, 9, 2, 9)
msort(l1)

val l2 = List(2, 1, 1, 3, 4, 9, 5)
msort(l2)

val l3 = List(9, 8, 7, 3, 2, 1)
msort(l3)

val l4 = List(9, 9, 8, 8, 7, 7, 3, 3, 2, 2, 1, 1)
msort(l4)

val l5 = List(8, 15, 3, 1)
msort(l5)

val l6 = List(1, 5, 4, 8, 10, 2, 6, 9, 3, 7)
msort(l6)

val l7 = List(1, 5, 4, 8, 10, 2, 6, 9, 12, 11, 3, 7)
msort(l7)