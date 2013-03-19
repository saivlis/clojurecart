(ns clojurecart.handler
  (:use compojure.core clojurecart.resources clojurecart.html clojurecart.url clojurecart.mediatypes)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [com.twinql.clojure.conneg :as conneg]))

(defn render-response [resource headers method]
  (if (nil? resource)
    {:status 404}
    (let [format (conneg/best-allowed-content-type 
                   (get headers "accept") 
                   (-> resource
                     (get :get)
                     (get :produced)))]
      (if (nil? format)
        {:status 406}
        (-> resource
          (get method)
          (get :response)
          (get format))))))

(defroutes app-routes
  (GET "/" 
       {:keys [headers request-method]} 
       (render-response (root) headers request-method))
  (GET users-route 
       {:keys [headers request-method]} 
       (render-response (allusers) headers request-method))
  (GET user-route 
       {:keys [headers request-method params]} 
       (render-response (user (Integer/parseInt (:id params))) headers request-method))
  (GET carts-of-user-route
       {:keys [headers request-method params]} 
       (render-response (carts-of-user (Integer/parseInt (:id params))) headers request-method))
  (GET cart-route
        {:keys [headers request-method params]} 
        (render-response (cart (Integer/parseInt (:id params))) headers request-method)) 
  (route/not-found "Route Not Found"))


(def app
  (handler/site app-routes))
