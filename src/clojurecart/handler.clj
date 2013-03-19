(ns clojurecart.handler
  (:use compojure.core clojurecart.resources clojurecart.html clojurecart.url)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn render-response [resource format] (format (:representations resource)))


(defroutes app-routes
  (GET "/" [] "HALLO")
  (GET (users-route) [] (render-response (allusers) :html))
  (GET (user-route) [id] (render-response (user id) :html))
  (GET (carts-of-user-route) [id] (render-response (carts-of-user id) :html))
  (GET (cart-route) [id] (render-response (cart id) :html)) 
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
