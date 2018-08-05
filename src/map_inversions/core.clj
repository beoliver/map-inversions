(ns map-inversions.core
  (:import clojure.lang.PersistentHashMap))

(defn invert-one-to-one
  "
  Returns a map that uses the vals as the keys and keys as vals
  "
  [m]
  (persistent! (reduce (fn [m [k v]] (assoc! m v k))
                       (transient PersistentHashMap/EMPTY) m)))

(defn invert-one-to-many
  "
  Returns a `many-to-one` mapping.
  A map is `one-to-many` if the `vals` are collections and all collections
  are disjoint - i.e the union of any two values is empty.

  (invert-one-to-many {1 #{:b :a}, 2 #{:c}}) => {:a 1 :b 1 :c 2}
  "
  [m]
  (persistent!
   (reduce (fn [m [k vs]] (reduce (fn [m v] (assoc! m v k)) m vs))
           (transient PersistentHashMap/EMPTY) m)))

(defn invert-many-to-one
  "
  Returns a `one-to-many` mapping.
  A map is `many-to-one` if more than one key is associatied with the same value.

  An optional `to` can be supplied - this must be conjable i.e nil, [], #{} ...
  If no element is supplied then it defaults to using [].

  If you have duplicate values then this function will not discard any information.

  (invert-many-to-one {:a 1 :b 1 :c 2}) => {1 [:b :a], 2 [:c]}
  "
  ([m] (invert-many-to-one m []))
  ([m to]
   (persistent!
    (reduce (fn [m [k v]]
              (assoc! m v (conj (get m v to) k)))
            (transient PersistentHashMap/EMPTY) m))))

(defn invert-many-to-many
  "
  Values must be collections.
  Returns a map whose values are collections of the current keys.
  An optional `to` can be supplied - this must be conjable i.e nil, [], #{} ...
  If no element is supplied then it defaults to using [].

  Values are collections and there exists at least two values whose intersection is not empty.
  If your values are collections then it is best to assume that they fall into this category.

  (invert-many-to-many {:a #{1 2} :b #{1 3} :c #{3 4}}) => {1 #{:b :a}, 2 #{:a}, 3 #{:c :b}, 4 #{:c}}
  "
  ([m] (invert-many-to-many m []))
  ([m to]
   (persistent!
    (reduce (fn [m [k vs]]
              (reduce (fn [m v] (assoc! m v (conj (get m v to) k))) m vs))
            (transient PersistentHashMap/EMPTY) m))))
