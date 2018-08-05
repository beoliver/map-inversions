# map-inversions

Simple transformations for swapping keys and values in clojure maps

[![Clojars Project](https://img.shields.io/clojars/v/beoliver/map-inversions.svg)](https://clojars.org/beoliver/map-inversions)

## Usage

Provides four transformation functions:
```clojure
(require '[map-inversions.core :as i])
```

### invert-one-to-one

A relation is `one-to-one` if each key is associated with a unique val.
The result of inverting a one-to-one map is a one-to-one map.

```clojure
user> (i/invert-one-to-one {:a 1 :b 2 :c 3})
{1 :a, 2 :b, 3 :c}

user> (i/invert-one-to-one {:a 1 :b 1 :c 3})
{1 :b, 3 :c}
```

### invert-many-to-one

A relation is `many-to-one` if there are two or more keys that
are associated with the same value - If your values are not unique,
then you probably want this...

```clojure
user> (i/invert-many-to-one {:a 1 :b 2 :c 2})
{1 [:a], 2 [:b :c]}

user> (i/invert-many-to-one {:a 1 :b 2 :c 3})
{1 [:a], 3 [:c] 2 [:b]}

;; note that you can pass an optional item that will serve as
;; the base coll for calls to conj during the construction
;; for performance the default is []

user> (i/invert-many-to-one {:a 1 :b 2 :c 2} #{})
{1 #{:a}, 2 #{:c :b}}

user> (i/invert-many-to-one {:a 1 :b 2 :c 2} nil)
{1 (:a), 2 (:c :b)}
```

### invert-one-to-many

A relation is `one-to-many` if each key is associated with a collection of values
and all collections are disjoint - i.e the union of any two values is empty.

```clojure
user> (i/invert-one-to-many {:a [1 2] :b #{3 4 5} :c (list 6)})
{1 :a, 2 :a, 4 :b, 3 :b, 5 :b, 6 :c}
```

### invert-many-to-many
If your values are collections and they contain duplicates,
then you just want to use this

```clojure
user> (i/invert-many-to-many {:a [1 2] :b #{3 2 5} :c (list 1 6)})
{1 [:a :c], 6 [:c], 3 [:b], 2 [:a :b], 5 [:b]}

;; again a constructor can be passed
user> (i/invert-many-to-many {:a [1 2] :b #{3 2 5} :c (list 1 6)} nil)
{1 (:c :a), 2 (:b :a), 3 (:b), 5 (:b), 6 (:c)}

user> (i/invert-many-to-many {:a [1 2] :b #{3 2 5} :c (list 1 6)} [:ok])
{1 [:ok :a :c], 2 [:ok :a :b], 3 [:ok :b], 5 [:ok :b], 6 [:ok :c]}
```