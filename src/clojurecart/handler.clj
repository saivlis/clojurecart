(ns clojurecart.handler
  (:use compojure.core clojurecart.resources clojurecart.html clojurecart.url clojurecart.mediatypes)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [com.twinql.clojure.conneg :as conneg]
            [ring.util.response :as ring]))

(defn render-response [resource headers]
  (if (nil? resource)
    {:status 404}
    (let [format (conneg/best-allowed-content-type 
                   (get headers "accept") 
                   (-> resource
                     (get :produced)))
          status (get (:response resource) :status 200)]
      (if (nil? format)
        {:status 406}
        (-> (ring/response (-> resource
                 (get :response)
                 (get format)))
          (ring/content-type (mediatype-to-s format))
          (ring/status status))))))

(defroutes app-routes
  (GET "/" 
       [:as r]
       (let [headers (:headers r)
             res ((:request-method r) (root))]
         (render-response res headers)))
  (GET users-route 
       [:as r]
       (let [headers (:headers r)
             res ((:request-method r) (allusers))]
         (render-response res headers)))
  (GET user-route 
       [id :as r]
       (let [headers (:headers r)
             res ((:request-method r) (user id))]
         (render-response res headers)))
  (GET carts-of-user-route
       [id :as r]
       (let [headers (:headers r)
             res ((:request-method r) (carts-of-user id))]
         (render-response res headers)))
  (GET cart-route
       [id :as r]
       (let [headers (:headers r)
             res ((:request-method r) (cart id))]
         (render-response res headers)))
  (GET "/favicon.ico"
       []
       (ring/file-response "data/image/123.png"))
  (route/not-found "Route Not Found"))

(def app
  (handler/site app-routes))
