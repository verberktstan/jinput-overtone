(ns jinput-overtone.core
  (:use [overtone.libs.handlers :as handlers])
  (:use [clojure.math.numeric-tower :only [abs]])
  (import
    (net.java.games.input ControllerEnvironment Component Controller)))

(def DELAY 30)

(def NO_DIFFS {})

(def ZERO 0.0)

(def NEAR_ZERO 0.1)

(def CONSIDER_SAME 0.1)

(def LIMIT 1.0)

(defonce handler-pool (handlers/mk-handler-pool "Jinput Event Handlers"))

(defn button [n] (str "Button " n))

(def BUTTON0 (button 0))
(def BUTTON1 (button 1))
(def BUTTON2 (button 2))
(def BUTTON3 (button 3))
(def BUTTON4 (button 4))
(def BUTTON5 (button 5))
(def BUTTON6 (button 6))
(def BUTTON7 (button 7))
(def BUTTON8 (button 8))
(def BUTTON9 (button 9))
(def BUTTON10 (button 10))
(def BUTTON11 (button 11))
(def BUTTON12 (button 12))
(def BUTTON13 (button 13))
(def BUTTON14 (button 14))
(def BUTTON15 (button 15))

(def HAT "Hat Switch")

(def X-AXIS "X Axis")
(def Y-AXIS "Y Axis")
(def Z-AXIS "Z Axis")

(def HAT_DIRECTIONS {
                      :north-west 0.125
                      :north 0.25
                      :north-east 0.375
                      :east 0.5
                      :south-east 0.625
                      :south 0.75
                      :south-west 0.875
                      :west 1.0})

(def HAT_NW (:north-west HAT_DIRECTIONS))
(def HAT_N (:north HAT_DIRECTIONS))
(def HAT_NE (:north-east HAT_DIRECTIONS))
(def HAT_E (:east HAT_DIRECTIONS))
(def HAT_SE (:south-east HAT_DIRECTIONS))
(def HAT_S (:south HAT_DIRECTIONS))
(def HAT_SW (:south-west HAT_DIRECTIONS))
(def HAT_W (:west HAT_DIRECTIONS))

(defn controller-type [controller]
  (-> controller .getType .toString))

(defn gamepad? [controller]
  (= "Gamepad" (controller-type controller)))

(defn joystick? [controller]
  (= "Stick" (controller-type controller)))

(defn keyboard? [controller]
  (= "Keyboard" (controller-type controller)))

(defn mouse? [controller]
  (= "Mouse" (controller-type controller)))

(defn game-controller? [controller]
  (or
    (gamepad? controller)
    (joystick? controller)))

(defn find-name [item]
  (cond
    (instance? Component) (.getName item)
    (instance? Controller) (.getName item)
    :else (.toString item)))

(defn hat-direction? [direction value]
  (= direction value))

(defn hat-released? [value]
  (= 0.0 value))

(defn button-pressed? [value]
  (= 1.0 value))

(defn button-released? [value]
  (= 0.0 value))

(defn find-controllers
  ([] (.getControllers (ControllerEnvironment/getDefaultEnvironment)))
  ([fn] (filter fn (find-controllers))))

(defn find-gamepads []
  (find-controllers gamepad?))

(defn find-controller [pattern]
  (first (find-controllers #(re-find (re-pattern (str "(?i)" pattern)) (find-name %)))))

(defn message-path [controller component]
  [controller (find-name component)])

(defn poll-state [controller]
  (if (.poll controller)
    (into {} (map #(-> [% (.getPollData %)]) (.getComponents controller)))
    NO_DIFFS))

(defn poll-states [controllers]
  (into {} (map #(-> [% (poll-state %)]) controllers)))

(defn event-send [path value]
  (handlers/event handler-pool path :val value))

(defn controller-event-handlers
  "parameters are a controller and a map of components to functions"
  [controller handlers]
  (when controller
    (doseq [[k v] handlers]
      (handlers/add-handler handler-pool (message-path controller k) (keyword k) v))))

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

(defn event-loop-game []
  (loop-controllers (find-controllers game-controller?) #'event-send))

(defn start-input []
  (let [t (Thread. event-loop-game)]
    (.setDaemon t true)
    (.start t)
    t))

(defn stop-input [p]
  (.stop p))
