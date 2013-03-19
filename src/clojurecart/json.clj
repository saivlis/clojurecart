(ns clojurecart.json
  (:require [clojure.data.json :as json]))

(defn to-json [data] (json/write-str data))