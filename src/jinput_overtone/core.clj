(ns jinput-overtone.core
  (:require [overtone.config.log :as log]
            [overtone.libs.event :as evnt])
  (:use [clojure.math.numeric-tower :only [abs]])
  (import (net.java.games.input Component Controller)))

(def DELAY 30)

(def NO_DIFFS {})

(def ZERO 0.0)

(def NEAR_ZERO 0.1)

(def CONSIDER_SAME 0.1)

(def LIMIT 1.0)

(defn find-name [item]
  (cond
    (instance? Component) (.getName item)
    (instance? Controller) (.getName item)
    :else (.toString item)))

(defn message-path [controller component]
  [:jinput controller (find-name component)])

(defn poll-state [controller]
  (if (.poll controller)
    (into {} (map #(-> [% (.getPollData %)]) (.getComponents controller)))
    NO_DIFFS))

(defn poll-states [controllers]
  (into {} (map #(-> [% (poll-state %)]) controllers)))

(defn event-send [event-type value]
  (evnt/event event-type {:val value}))

(defn controller-event-handlers
  "parameters are a controller and a map of components to functions"
  [controller handlers]
  (when controller
    (doseq [[k v] handlers]
      (evnt/on-event (message-path controller k) v (keyword k)))))

(defn send-diffs
  "Send diffs if there are any"
  [sender controller diffs]
  (when (not= diffs NO_DIFFS)
    (println (.getName controller) diffs)
    (doseq [[k v] diffs]
      (sender (message-path controller k) v))))

(defn send-and-store [controller old-state sender diffs]
  (send-diffs sender controller diffs)
  [controller (conj old-state diffs)])

(defn different-values? [old new]
  (and
    (not= old new)
    (or
      (> (abs (- old new)) CONSIDER_SAME)
      (= LIMIT (abs new))
      (= ZERO (abs new)))))

(defn round-to-zero [[k v]]
  (if (< (abs v) NEAR_ZERO)
    [k ZERO]
    [k v]))

(defn diff-state
  "Find the differences between two states of a controller"
  [previous-state current-state]
  (into {}
    (map round-to-zero
      (filter
        (fn [[k v]] (different-values? (get previous-state k) v)) current-state))))

(defn new-state [controller old-state sender]
  (send-and-store controller old-state sender (diff-state old-state (poll-state controller))))

(defn loop-states [controllers sender old-states]
  (loop [states old-states]
    (Thread/sleep DELAY)
    (recur
      (into {}
        (map #(new-state % (get states %) sender) controllers)))))

(defn loop-controllers [controllers sender]
  (loop-states controllers sender (poll-states controllers)))
