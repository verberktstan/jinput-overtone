(ns jinput-overtone.loop
  (:use [jinput-overtone.core]
        [jinput-overtone.finders]
        [jinput-overtone.events]))

(defn loop-states
  ([controllers send-fn] (loop-states controllers send-fn (poll-states controllers)))
  ([controllers send-fn old-states]
    (loop [states old-states]
    (Thread/sleep DELAY)
    (recur
      (into {}
        (map #(new-state % (get states %) send-fn) controllers))))))

(defn event-loop-game []
  (loop-states (find-controllers game-controller?) #'send-diffs))

(defn start-input []
  (let [t (Thread. event-loop-game)]
    (.setDaemon t true)
    (.start t)
    t))

(defn stop-input [p]
  (.stop p))
