(ns clojurecart.resources
  (:use clojurecart.persistence clojurecart.html clojurecart.url clojurecart.json clojurecart.mediatypes))

(defn root []
  {:get
   {:produced #{html}
    :response (fn [request] {html (html-helper "Welcome to ClojureCart!" (html-link users-route {} "All Users"))})}})

(defn allusers []
  {:get 
   (let [data (get-all-users)]
     (if (nil? data)
       nil
       {:produced #{html json}
        :response (fn [request] {html (all-users-to-html
                          (->> data
                            (map get-user)
                            (map #(html-link user-route % (:name %)))))
                   json "TODO"})}))
   :post {:consumed #{urlenc}
          :produced #{html}
          :response (fn [request] 
                      (let [name (:name (:params request))
                            newId (create-user {:name name})]
                        {:status 201
                         html (user-to-html (get-user newId))}))}})

(defn user [id] 
  {:get
   (let [data (get-user id)] 
     (if (nil? data) 
       nil
       {:produced #{html json}
        :response (fn [request] {html (user-to-html data)
                   json (to-json data)})} ))})

(defn carts-of-user [id] 
  {:get 
   (let [data (get-carts-of-user id)] 
     (if (nil? data) 
       nil
       {:produced #{html json}
        :response (fn [request] {html (list-with-title-to-html
                          (str "Carts of " (:name (get-user id))) 
                          (->> data
                            (map get-cart)
                            (map #(html-link cart-route % (:description %)))))
                   json (to-json
                          (->> data
                            (map get-cart)
                            (map #(build-link cart-route %))))})}))
   :post {:consumed #{urlenc}
          :produced #{html}
          :response (fn [request] 
                      (let [description (:description (:params request))
                            newId (create-cart {:description description} id)]
                        {:status 201
                         html (cart-to-html (get-cart newId))}))}})

(defn cart [id] 
  {:get
   (let [data (get-cart id)] 
     (if (nil? data)
       nil
       {:produced #{html json}
        :response (fn [request] {html (cart-to-html data)
                   json (to-json data)})}))})

