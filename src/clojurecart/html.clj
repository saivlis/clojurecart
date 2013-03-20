(ns clojurecart.html
  (:use hiccup.core noir.core clojurecart.url hiccup.element))

(defn html-helper [title body] 
  (html 
    [:html
     [:head 
      [:title title]]
     [:body 
      [:h1 title] 
      body]]))

(defn html-link [url args content] (html (link-to (build-link url args) content)))


(defn create-link [url args] 
  (url-for* url args))

(defn list-to-html [list] 
  (list-with-title-to-html "List" list))

(defn list-with-title-to-html [title list] 
  (html-helper 
    title 
    (if (empty? list)
      "No entries found"
      (unordered-list list))))

(defn user-to-html [user] 
  (html-helper 
    "User" 
    (str (:name user) " " (html-link carts-of-user-route user "Carts of User"))))

(defn cart-to-html [cart] 
  (html-helper 
    "Cart" 
    (str (:description cart))))