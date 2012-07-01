(defproject jinput-overtone/jinput-overtone "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/math.numeric-tower "0.0.1"]
                 [overtone/midi-clj "0.4.0"]
                 [overtone "0.7.1"]
                 [midiutils-overtone "0.0.1-SNAPSHOT"]
                 [jinput "0.0.3"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-pprint "1.1.1"]]
  :description "Bringing JInput devices to Overtone, the Clojure music project")
