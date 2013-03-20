(ns clojurecart.persistence
  (:use clojurecart.couch)
  (:require [clojure.data.json :as json]))

(defn get-all [dbname]
  (let [db (get-db dbname)
        all (get-doc db "_all_docs")]
  (->> all
    :rows
    (map #(:id %)))))

(defn get-couch-doc [type id]
  (let [db (get-db type)
        doc (get-doc db id)
        did (:_id doc)]
  (-> doc
    (assoc :id did)
    (dissoc :_id))))

(defn get-all-users []
  (get-all "users"))

(defn get-user [id] 
  (get-couch-doc "users" id))

(defn get-cart [id] 
  (get-couch-doc "carts" id))

(defn get-carts-of-user [id]
  (get-view (get-db "carts") "_design/carts/_view/by_user" id))

(defn create-user [user]
  (create-doc (get-db "users") (json/write-str user)))

(defn create-cart [cart uid]
  (create-doc (get-db "carts") (json/write-str (assoc cart :uid uid))))

