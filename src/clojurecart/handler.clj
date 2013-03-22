(ns clojurecart.handler
  (:use compojure.core clojurecart.resources clojurecart.html clojurecart.url clojurecart.mediatypes html_forms_middleware)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :as core]
            [com.twinql.clojure.conneg :as conneg]
            [ring.util.response :as ring]))

(defn has-body? [meth]
  (contains? #{:post :put} meth))

(defn add-header [resp [name value]] (ring/header resp name value))

(defn add-headers [resp headers]
  (reduce add-header resp headers))

(defn render-response [res request]
  (let [headers (:headers request)
        meth (:request-method request)]
    (try 
    (if (nil? (meth res))
      {:status 405}
      (let [resource (meth res)
            format (conneg/best-allowed-content-type 
                     (get headers "accept") 
                     (-> resource
                       (get :produced)))
            consumable (set (map mediatype-to-s (:consumed resource)))]
        (if (and (has-body? meth) (not (contains? consumable (:content-type request))))
          {:status 415}
          (if (nil? format)
            {:status 406}
            (let [response ((:response resource) request)]
              (if (nil? response)
                {:status 404}
                (-> (ring/response 
                      (-> response
                        (get format)))
                  (ring/content-type (mediatype-to-s format))
                  (ring/status (get response :status 200))
                  (add-headers (:headers response)))))))))
  (catch java.net.ConnectException e {:status 503 :body (.getMessage e)})
  (catch clojurecart.exception.DatabaseException e {:status 500 :body (.getMessage e)}))))

(defroutes app-routes
  (ANY "/" 
       [:as request]
       (render-response (root) request))
  (ANY users-route 
       [:as request]
       (render-response (allusers) request))
  (ANY user-route 
       [id :as request]
       (render-response (user id) request))
  (ANY carts-of-user-route
       [id :as request]
       (render-response (carts-of-user id) request))
  (ANY cart-route
       [id :as request]
       (render-response (cart id) request))
  (GET "/favicon.ico"
       []
       (ring/file-response "data/image/123.png"))
  (route/not-found "Route Not Found"))

(def app
    (handler/site (wrap-html-put-delete-forms app-routes)))
