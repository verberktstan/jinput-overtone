(ns jinput-overtone.loop
  (:use [jinput-overtone.core]
        [jinput-overtone.finders]))

(defn event-loop-game []
  (loop-controllers (find-controllers game-controller?) #'event-send))

(defn start-input []
  (let [t (Thread. event-loop-game)]
    (.setDaemon t true)
    (.start t)
    t))

(defn stop-input [p]
  (.stop p))
