(ns clojurecart.html
  (:use hiccup.core noir.core clojurecart.url hiccup.element hiccup.form))

(defn html-helper [title body] 
  (html 
    [:html
     [:head 
      [:title title]]
     [:body 
      [:header (html-link users-route "All users")]
      [:h1 title] 
      body]]))

(defn html-link 
  ([url content] (html (link-to (build-link url) content)))
  ([url args content] (html (link-to (build-link url args) content))))


(defn create-link [url args] 
  (url-for* url args))

(defn list-with-title-to-html [title list] 
  (html-helper 
    title 
    (if (empty? list)
      "No entries found"
      (unordered-list list))))

(defn list-to-html [list] 
  (list-with-title-to-html "List" list))

(defn user-to-html [user] 
  (html-helper 
    "User"
    (html
      [:p (str (:name user) " " (html-link carts-of-user-route user "Carts of User"))]
      (form-to 
        [:post (build-link carts-of-user-route user)]
        (text-field "description")
        [:br]
        (submit-button "Create New Cart")))))

(defn cart-to-html [cart] 
  (html-helper 
    "Cart" 
    (html [:p (str "Description: " (:description cart))] 
          [:p (html-link user-route {:id (:uid cart)} "Owner")])))

(defn all-users-to-html [list]
  (html-helper 
    "All Users" 
    (html 
      (unordered-list list)
      (form-to 
        [:post (build-link users-route)]
        (text-field "name")
        [:br]
        (submit-button "Create User")))))