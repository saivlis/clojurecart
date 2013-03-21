(ns clojurecart.persistence
  (:use clojurecart.couch)
  (:require [clojure.data.json :as json]))

(defn get-all [dbname]
  (try 
    (let [db (get-db dbname)
        all (get-doc db "_all_docs")]
    (->> all
    :rows
    (map #(:id %))))
  (catch java.net.ConnectException e (throw (new Exception "Database not available")))))

(defn get-couch-doc [type id]
  (try 
    (let [db (get-db type)
          doc (get-doc db id)
          did (:_id doc)]
      (if (nil? doc)
        nil
        (-> doc
          (assoc :id did)
          (dissoc :_id))))
  (catch java.net.ConnectException e (throw (new Exception "Database not available" e)))))

(defn get-couch-view 
  ([dbname view] 
    (try (get-view (get-db dbname) view)
      (catch java.net.ConnectException e (throw (new Exception "Database not available" e)))))
  ([dbname view key] 
    (try (get-view (get-db dbname) view key)
      (catch java.net.ConnectException e (throw (new Exception "Database not available" e))))))

(defn get-all-users []
  (get-all "users"))

(defn get-user [id] 
  (get-couch-doc "users" id))

(defn get-cart [id] 
  (get-couch-doc "carts" id))

(defn get-carts-of-user [id]
  (get-couch-view  "carts" "_design/carts/_view/by_user" id))

(defn create-user [user]
  (:id (json/read-json (:body (create-doc (get-db "users") (json/write-str user))))))

(defn create-cart [cart uid]
  (:id (json/read-json (:body (create-doc (get-db "carts") (json/write-str (assoc cart :uid uid)))))))

