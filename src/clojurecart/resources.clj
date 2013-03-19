(ns clojurecart.resources
  (:use clojurecart.persistence clojurecart.html clojurecart.url clojurecart.json clojurecart.mediatypes))

(defn resource [representations]
  {:representations representations})

(defn root []
  {:get
   {:produced #{html}
    :response {html (html-helper "Welcome to ClojureCart!" (html-link users-route {} "All Users"))}}})

(defn allusers []
  (let [data '(1 2 3)]
    (if (nil? data)
      nil
      {:get 
       {:produced #{html json}
        :response {html (list-with-title-to-html 
                            "All Users" 
                            (->> data
                              (map get-user)
                              (map #(html-link user-route % (:name %)))))
                   json "TODO"}}})))

(defn user [id] 
  (let [data (get-user id)] 
    (if (nil? data) 
      nil
      {:get
       {:produced #{html json}
        :response {html (user-to-html data)
                   json (to-json data)}}})))

(defn carts-of-user [id] 
  (let [data '(2 3 4 5)] 
    (if (nil? data) 
      nil
      {:get 
       {:produced #{html json}
        :response {html (list-with-title-to-html
                            (str "Carts of " (:name (get-user id))) 
                            (->> data
                              (map get-cart)
                              (map #(html-link cart-route % (:description %)))))
                   json (to-json
                            (->> data
                              (map get-cart)
                              (map #(build-link cart-route %))))}}})))

(defn cart [id] 
  (let [data (get-cart id)] 
    (if (nil? data)
      nil
      {:get
       {:produced #{html json}
        :response {html (cart-to-html data)
                   json (to-json data)}}})))

