(ns clojurecart.persistence)

(defn get-user [id] (if (> id 10) nil {:name "Hans" :id id}))

(defn get-cart [id] {:description (str "Mein Korb " id) :id id})