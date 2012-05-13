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

(deftest zeroes-test
  (is (= ["a" ZERO] (round-to-zero ["a" 0.001])))
  (is (= ["a" ZERO] (round-to-zero ["a" 0.01])))
  (is (= ["a" NEAR_ZERO] (round-to-zero ["a" NEAR_ZERO])))
  (let [not-zero (+ NEAR_ZERO (/ NEAR_ZERO 100))]
    (is (= ["a" not-zero] (round-to-zero ["a" not-zero])))))
