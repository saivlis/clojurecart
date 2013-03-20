(ns clojurecart.couch
  (:use clojurecart.httpclient)
  (:require [clojure.data.json :as json]))

(def db-location "http://localhost:5984/")

(defn get-db [name] 
  (let [dbname (str db-location name)]
    (if (= 404
           (->>
             (http-get dbname)
             (:status)))
      (do 
        (http-put dbname)
        dbname)
      dbname)))

(defn get-doc [db docId]
  (let [doc (http-get (str db "/" docId))]
    (if (= 200 (:status doc))
      (json/read-json (:body doc))
      nil)))

(defn get-view
  ([db view] 
    (let [doc (get-doc db view)]
      (->> doc
        (:rows)
        (map #(:id %)))))
  ([db view key] 
    (let [doc (get-doc db view)]
      (->> doc
        (:rows)
        (filter #(= key (:key %)))
        (map #(:id %))))))

