(ns clojurecart.persistence
  (:use clojurecart.couch
        clojurecart.httpclient)
  (:require [clojure.data.json :as json]))

(defn get-all [dbname]
    (let [db (get-db dbname)
        all (get-doc db "_all_docs")]
    (->> all
    :rows
    (map #(:id %)))))

(defn get-couch-doc 
  ([type id]
    (let [db (get-db type)
          doc (get-doc db id)
          did (:_id doc)]
      (if (nil? doc)
        nil
        (-> doc
          (assoc :id did)
          (dissoc :_id)))))
  ([type id rev]
    (let [db (get-db type)
          doc (get-doc db id rev)
          did (:_id doc)]
      (if (nil? doc)
        nil
        (-> doc
          (assoc :id did)
          (dissoc :_id))))))

(defn delete-couch-doc [type id rev]
    (let [db (get-db type)]
      (delete-doc db id rev)))

(defn update-couch-doc [type id newDoc]
    (let [db (get-db type)
          rev (update-doc db id newDoc)]
      (get-couch-doc type id rev)))

(defn get-couch-view 
  ([dbname view] 
(get-view (get-db dbname) view))
  ([dbname view key] 
    (get-view (get-db dbname) view key)))

(defn get-all-users []
  (get-all "users"))

(defn get-user [id] 
  (get-couch-doc "users" id))

(defn get-cart [id] 
  (get-couch-doc "carts" id))

(defn delete-cart [id rev]
  (try 
    (delete-couch-doc "carts" id rev)
    (catch clojurecart.exception.ConflictException e (throw (new clojurecart.exception.ConflictException "Cart has been changed recently")))
    (catch clojurecart.exception.DatabaseException e (throw (new clojurecart.exception.DatabaseException "Cart could not be deleted")))))

(defn update-cart [id newCart]
  (try
    (update-couch-doc "carts" id newCart)
    (catch clojurecart.exception.ConflictException e (throw (new clojurecart.exception.ConflictException "Cart has been changed recently")))
    (catch clojurecart.exception.DatabaseException e (throw (new clojurecart.exception.DatabaseException "Cart could not be updated: ")))))

(defn get-carts-of-user [id]
  (get-couch-view  "carts" "_design/carts/_view/by_user" id))

(defn create-user [user]
  (:id (json/read-json (:body (create-doc (get-db "users") (json/write-str user))))))

(defn create-cart [cart uid]
  (:id (json/read-json (:body (create-doc (get-db "carts") (json/write-str (assoc cart :uid uid)))))))

(defn database-running? [] 
  (try
    (http-get db-location)
    true
  (catch java.net.ConnectException e false)))

(defn check-db [name] 
  (let [dbname (str db-location name)]
    (if (= 404
           (->>
             (http-get dbname)
             (:status)))
      (do 
        (http-put dbname)
        dbname)
      dbname)))

(defn check-carts-views []
  (if (= 404 (:status (http-get (str (get-db "carts") "/" "_design/carts"))))
    (http-put (str (get-db "carts") "/" "_design/carts")
                         "application/json"
                         (json/write-str 
                           {"_id" "_design/carts"
                            "views" {
                                     "all" { "map" "function(doc) { emit(null, doc) }"}
                                     "by_user" {"map" "function(doc) { emit(doc.uid, doc) }"}
                                     }
                            "language" "javascript"}))))
(defn check-databases-and-view [] 
  (check-db "users")
  (check-db "carts")
  (check-carts-views))

(defn init-db []
  (if (database-running?)
    (check-databases-and-view)
    (.println System/out "ERROR: No Database running!")))