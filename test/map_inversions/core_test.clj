(ns map-inversions.core-test
  (:require  [clojure.test :refer :all]
             [map-inversions.core :as m]))

(def a-z (map char (range 97 123)))

(def one-to-one-dataset
  (zipmap a-z (range)))

(def many-to-one-dataset
  (zipmap a-z (cycle (range 5))))

(def one-to-many-dataset
  (m/invert-many-to-one many-to-one-dataset))

(def one-to-one-identity
  (comp m/invert-one-to-one m/invert-one-to-one))

(def many-to-one-identity
  (comp m/invert-one-to-many m/invert-many-to-one))

(def one-to-many-identity
  (comp m/invert-many-to-one m/invert-one-to-many))

(def many-to-many-identity
  (comp m/invert-many-to-many m/invert-many-to-many))

(deftest one-to-one-test
  (is (= one-to-one-dataset
         (one-to-one-identity one-to-one-dataset))))

(deftest many-to-one-test
  (is (= many-to-one-dataset
         (many-to-one-identity many-to-one-dataset))))

(deftest one-to-many-test
  (is (= one-to-many-dataset
         (one-to-many-identity one-to-many-dataset))))
