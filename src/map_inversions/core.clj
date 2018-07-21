(ns map-inversions.core)

(defn invert-unique
  "
  Returns a map that uses the values as the keys.

  (invert-unique {:a 1 :b 2 :c 3}) => {1 :a 2 :b 3 :c}
  (invert-unique [[:a 1] [:b 2] [:c 3]]) => {1 :a 2 :b 3 :c}
  "
  [m]
  (persistent! (reduce (fn [m [k v]] (assoc! m v k)) (transient {}) m)))

(defn invert
  "
  Returns a one-to-many mapping - a map where the values are disjoint collections.
  An optional `to` can be supplied - this must be conjable i.e nil, [] ...
  If no element is supplied then it defaults to using #{}.

  If you have duplicate values then this function will not discard any information.
  If the values of the map are *not* unique then this is the function for you!

  (invert-many-to-one {:a 1 :b 1 :c 2}) => {1 #{:b :a}, 2 #{:c}}
  "
  ([m] (invert m #{}))
  ([m to]
   (persistent!
    (reduce (fn [m [k v]]
              (assoc! m v (conj (get m v to) k)))
            (transient {}) m))))

(defn invert-disjoint-colls
  "
  Values must be collections.
  Returns a map that uses the current keys as values.
  This requires the initial values (colls) to be disjoint.

  When to use?
  1. values are collections.
  2. no two collections contain the same value.

  (invert-disjoint-collections {1 #{:b :a}, 2 #{:c}}) => {:a 1 :b 1 :c 2}
  "
  [m]
  (persistent!
   (reduce (fn [m [k vs]] (reduce (fn [m v] (assoc! m v k)) m vs))
           (transient {}) m)))

(defn invert-colls
  "
  Values must be collections.
  Returns a map whose values are collections of the current keys.
  An optional `to` can be supplied - this must be conjable i.e nil, [] ...
  If no element is supplied then it defaults to using #{}.

  Values are collections and there exists at least two values whose intersection is not empty.
  If your values are collections then it is best to assume that they fall into this category.

  (invert-many-to-many {:a #{1 2} :b #{1 3} :c #{3 4}}) => {1 #{:b :a}, 2 #{:a}, 3 #{:c :b}, 4 #{:c}}
  "
  ([m] (invert-colls m #{}))
  ([m to]
   (persistent!
    (reduce (fn [m [k vs]]
              (reduce (fn [m v] (assoc! m v (conj (get m v to) k))) m vs))
            (transient {}) m))))
