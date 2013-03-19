(ns clojurecart.resources
  (:use clojurecart.persistence clojurecart.html clojurecart.url clojurecart.json))

(defn resource [representations]
  {:representations representations})

(defn allusers [] 
  (let [data '(1 2 3)] 
    (resource {:html 
               (list-with-title-to-html 
                 "All Users" 
                 (->> data
                   (map get-user)
                   (map #(html-link (user-route) % (:name %)))))})))

(defn user [id] 
  (let [data (get-user id)] 
    (resource 
      {:html (user-to-html data)
       :json (to-json data)})))

(defn carts-of-user [id] 
  (let [data '(2 3 4 5)] 
    (resource 
      {:html (list-with-title-to-html
               (str "Carts of " (:name (get-user id))) 
               (->> data
                 (map get-cart)
                 (map #(html-link (cart-route) % (:description %)))))
       :json (to-json
               (->> data
                 (map get-cart)
                 (map #(build-link (cart-route) %))))})))

(defn cart [id] 
  (let [data (get-cart id)] 
    (resource 
      {:html (cart-to-html data)})))

