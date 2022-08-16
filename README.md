# Clojure REPL
## Background
[Clojure](https://clojure.org/) is a functional programming language that runs on the Java Virtual Machine. As a Lisp dialect, Clojure code is composed of Clojure collections such as lists and vectors. An introduction of Clojure syntax can be found [here](https://learnxinyminutes.com/docs/clojure/).
## Implementation
- For simplicity, the only supported types are integers, booleans, `nil`, and lists. Suprisingly a fair bit can still be done with this limitation.
- Partial support for high-order functions. Functions are able to take other functions as arguments, and you can write and run your own `map`, `reduce` and `filter` functions in the REPL. Currently functions can't return functions as results.
- Anonymous functions are supported, but there is no support for closures or function currying.
- Lists are the only supported collection. Code that requires use of a vector (eg. for function parameters) can be writeen as Clojure vectors, but internally they will be handled like lists.
- The provided functions do not have nullary operation except for lists. e.g. `(+)` and `(or)` are not implemented. The use cases for these are rare.
- No support for macros.
- No support for reader macros, e.g. an empty list can't be written as `'()`, instead write it as `(list)`.
- No support for consing onto `nil` like `(cons 1 nil)` to create a list.
- The included functions are:

 Maths
  - `+`
  - `-`
  - `*`
  - `/`
  - `mod`
    
 Boolean and comparison
  - `and`
  - `or`
  - `not`
  - `=`
  - `>`
  - `<`

 Lists
  - `cons`
  - `empty?`
  - `first`
  - `list`
  - `rest`
 
 Definitions
  - `def`
  - `defn`

 Anonymous Function
  - `fn`
 
 Conditional
  - `if`