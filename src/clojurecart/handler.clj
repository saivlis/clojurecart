(ns clojurecart.handler
  (:use compojure.core clojurecart.resources clojurecart.html clojurecart.url clojurecart.mediatypes)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [com.twinql.clojure.conneg :as conneg]
            [ring.util.response :as ring]))

(defn has-body? [meth]
  (contains? #{:post :put} meth))

(defn render-response [res request]
  (let [headers (:headers request)
        meth (:request-method request)
        resource (meth res)
        response (:response resource)
        format (conneg/best-allowed-content-type 
                 (get headers "accept") 
                 (-> resource
                   (get :produced)))
        status (get response :status 200)
        consumable (set (map mediatype-to-s (:consumed resource)))]
    (if (nil? resource)
      {:status 404}
      (if (and (has-body? meth) (not (contains? consumable (:content-type request))))
        {:status 415}
        (if (nil? format)
          {:status 406}
          (-> (ring/response 
                (-> response
                  (get format)))
            (ring/content-type (mediatype-to-s format))
            (ring/status status)))))))

(defroutes app-routes
  (GET "/" 
       [:as request]
       (let [res (root request)]
         (render-response res request)))
  (GET users-route 
       [:as request]
       (let [res (allusers request)]
         (render-response res request)))
  (POST users-route
        [:as request]
         (let [res (allusers request)]
         (render-response res request)))
  (GET user-route 
       [id :as request]
       (let [res (user request id)]
         (render-response res request)))
  (GET carts-of-user-route
       [id :as request]
       (let [res (carts-of-user request id)]
         (render-response res request)))
  (GET cart-route
       [id :as request]
       (let [res (cart request id)]
         (render-response res request)))
  (GET "/favicon.ico"
       []
       (ring/file-response "data/image/123.png"))
  (route/not-found "Route Not Found"))

(def app
  (handler/site app-routes))
