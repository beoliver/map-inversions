# map-inversions

Simple transformations for swapping keys and values in clojure maps

[![Clojars Project](https://img.shields.io/clojars/v/beoliver/map-inversions.svg)](https://clojars.org/beoliver/map-inversions)

## Usage

Provides four transformation functions:
```clojure
(require '[map-inversions.core :as inv])
```
the first example is for maps that have unique values.

### invert-unique
```clojure
user> (inv/invert-unique {:a 1 :b 2 :c 3})
{1 :a, 2 :b, 3 :c}

user> (inv/invert-unique [[:a 1] [:b 2] [:b 3]])
{1 :a, 2 :b, 3 :b}

user> (inv/invert-unique {:a 1 :b 1 :c 3})
{1 :b, 3 :c}
```

### invert
if your values are not unique, then you probably want this...
```clojure
user> (inv/invert {:a 1 :b 2 :c 2})
{1 #{:a}, 2 #{:b :c}}

;; note that you can pass an optional item that will serve as
;; the base coll for calls to conj during the construction

user> (inv/invert {:a 1 :b 2 :c 2} [])
{1 [:a], 2 [:b :c]}

user> (inv/invert {:a 1 :b 2 :c 2} nil)
{1 (:a), 2 (:c :b)}
```

## invert-disjoint-colls
If your values are collections and they are disjoint then
the inversion is a many to one mapping
```clojure
user> (inv/invert-disjoint-colls {:a [1 2] :b #{3 4 5} :c (list 6)})
{1 :a, 2 :a, 4 :b, 3 :b, 5 :b, 6 :c}
```

## invert-colls
If your values are collections and they contain duplicates,
then you just want to use this
```clojure
user> (inv/invert-colls {:a [1 2] :b #{3 2 5} :c (list 1 6)})
{1 #{:c :a}, 2 #{:b :a}, 3 #{:b}, 5 #{:b}, 6 #{:c}

;; again a constructor can be passed
user> (inv/invert-colls {:a [1 2] :b #{3 2 5} :c (list 1 6)} nil)
{1 (:c :a), 2 (:b :a), 3 (:b), 5 (:b), 6 (:c)}

user> (inv/invert-colls {:a [1 2] :b #{3 2 5} :c (list 1 6)} [:ok])
{1 [:ok :a :c], 2 [:ok :a :b], 3 [:ok :b], 5 [:ok :b], 6 [:ok :c]}
```