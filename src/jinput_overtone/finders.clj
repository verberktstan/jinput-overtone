(ns jinput-overtone.finders
  (:use [jinput-overtone.core])
  (import (net.java.games.input ControllerEnvironment)))

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

(defn find-controllers
  ([] (.getControllers (ControllerEnvironment/getDefaultEnvironment)))
  ([fn] (filter fn (find-controllers))))

(defn find-gamepads []
  (find-controllers gamepad?))

(defn find-controller [pattern]
  (first (find-controllers #(re-find (re-pattern (str "(?i)" pattern)) (find-name %)))))
