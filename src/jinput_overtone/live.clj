(ns jinput-overtone.live
  (:use [jinput-overtone.core]))

(def t (start-input))

(defn stop-live []
  (stop-input t))

