(defproject clojurecart "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.2"]
                 [org.clojure/data.json "0.2.1"]
                 [com.twinql.clojure/clj-conneg "1.1.0"]
                 [ring "1.1.8"]
                 [clj-http "0.6.5"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler clojurecart.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
