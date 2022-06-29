# Boolean Expressions

## Warmup

A recursive function satisfying:
- f(x) = f(x-1)+f(x-1)
- f(0) = 1
Can be written most straightforwardly as
```scala
def f(x: Integer): Integer = if x == 0 then 1 else f(x-1) + f(x-1)
```

This solution has time complexity O(2^N) (and space complexity O(N));
an obvious improvement would be to replace `f(x-1) + f(x-1)` with `2 * f(x-1)`,
which would bring it down to O(N), but we can see that the function computes
2^x so abandoning recursion we can simply write it as
```scala
def f(x: Integer): Integer = 1 << x
```
Which given a reasonable language implementation is constant time (as long as we don't
want to support `BigInt`).

If we have to keep the recursion, I'd go for
```scala
def f(x: Integer): Integer = {
  def h(x: Integer, acc: Integer): Integer = if x == 0 then acc else h(x-1, acc*2)
  h(x,1)
}
```
Which is tail-call-optimizable and should result in O(1) space usage.

