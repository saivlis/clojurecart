(ns clojurecart.url
  (:use noir.core))

(def baseurl "http://localhost:3000")

(defn build-link 
  ([url] (str baseurl url))
  ([url args] (str baseurl (url-for* url args))))

(def users-route "/users")
(def user-route "/user/:id")
(def carts-of-user-route "/user/:id/carts")
(def cart-route "/cart/:id")