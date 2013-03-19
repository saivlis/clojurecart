(ns clojurecart.handler
  (:use compojure.core clojurecart.resources clojurecart.html clojurecart.url clojurecart.mediatypes)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [com.twinql.clojure.conneg :as conneg]))

(defn render-response [resource headers]
  (if (nil? resource)
    {:status 404}
    (let [format (conneg/best-allowed-content-type 
                   (get headers "accept") 
                   (-> resource
                     (get :produced)))]
      (if (nil? format)
        {:status 406}
        (-> resource
          (get :response)
          (get format))))))

(defroutes app-routes
  (GET "/" 
       {:keys [headers request-method]} 
       (render-response (request-method (root)) headers))
  (GET users-route 
       {:keys [headers request-method]} 
       (render-response (request-method (allusers)) headers))
  (GET user-route 
       {:keys [headers request-method params]} 
       (render-response (request-method (user (Integer/parseInt (:id params)))) headers))
  (GET carts-of-user-route
       {:keys [headers request-method params]} 
       (render-response (request-method (carts-of-user (Integer/parseInt (:id params)))) headers))
  (GET cart-route
        {:keys [headers request-method params]} 
        (render-response (request-method (cart (Integer/parseInt (:id params)))) headers)) 
  (route/not-found "Route Not Found"))


(def app
  (handler/site app-routes))
