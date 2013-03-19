(ns clojurecart.persistence)

(defn get-user [id] {:name "Hans" :id id})

(defn get-cart [id] {:description (str "Mein Korb " id) :id id})