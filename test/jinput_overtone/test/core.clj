(ns jinput-overtone.test.core
  (:use [clojure.test])
  (:use [jinput-overtone.core]))

(deftest values-test
  (is (= false (different-values? 0.5 0.5)))
  (is (= false (different-values? 0.0 0.01)))
  (is (= true (different-values? 0.01 0.0)))
  (is (= false (different-values? 0.1 0.1099)))
  (is (= true (different-values? 0.9999 1.0)))
  (is (= true (different-values? -0.9999 -1.0)))
  (is (= true (different-values? -1.0 1.0))))
