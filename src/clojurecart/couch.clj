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

(defn get-doc 
  ([db docId]
  (let [doc (http-get (str db "/" docId))]
    (if (= 200 (:status doc))
      (json/read-json (:body doc))
      nil)))
  ([db docId rev]
    (let [doc (http-get (str db "/" docId "?rev=" rev))]
    (if (= 200 (:status doc))
      (json/read-json (:body doc))
      nil))))

(defn delete-doc [db docId rev]
  (let [doc (http-delete (str db "/" docId "?rev=" rev))]
    (if (= 200 (:status doc))
      true
      (if (= 409 (:status doc))
        (throw (new clojurecart.exception.ConflictException "The document has been changed recently."))       
        (throw (new clojurecart.exception.DatabaseException (:error doc)))))))

(defn update-doc [db docId newDoc]
  (let [doc (http-put (str db "/" docId) "application/json" (json/write-str newDoc))]
    (if (= 201 (:status doc))
      (let [body (json/read-json (:body doc))
            rev (:rev body)]
        rev)
      (if (= 409 (:status doc))
        (throw (new clojurecart.exception.ConflictException "The document has been changed recently."))       
        (throw (new clojurecart.exception.DatabaseException (:error doc)))))))

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

(defn create-doc [db doc]
  (http-post db "application/json" doc))