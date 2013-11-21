(defproject jinput-overtone/jinput-overtone "0.0.2-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/math.numeric-tower "0.0.2"]
                 [overtone/midi-clj "0.5.0"]
                 [overtone/at-at "1.2.0"]
                 [overtone "0.8.0"]
                 [midiutils-overtone "0.0.1-SNAPSHOT"]
                 [jinput "0.0.3"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-pprint "1.1.1"]
            [lein-kibit "0.0.8"]]
  :description "Bringing JInput devices to Overtone, the Clojure music project"
  :license {:name "Eclipse Public License 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "https://github.com/gavilancomun/jinput-overtone")
