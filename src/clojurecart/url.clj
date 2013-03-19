(ns clojurecart.url
  (:use noir.core))

(defn baseurl [] "http://localhost:3000")

(defn build-link [url args] (str (baseurl) (url-for* url args)))

(defn users-route [] "/users")
(defn user-route [] "/user/:id")
(defn carts-of-user-route [] "/user/:id/carts")
(defn cart-route [] "/cart/:id")