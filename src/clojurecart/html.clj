(ns clojurecart.html
  (:use hiccup.core noir.core clojurecart.url hiccup.element hiccup.form))

(defn html-link 
  ([url content] (html (link-to (build-link url) content)))
  ([url args content] (html (link-to (build-link url args) content))))

(defn html-helper [title body] 
  (html 
    [:html
     [:head 
      [:title title]]
     [:body 
      [:header (html-link users-route "All users")]
      [:h1 title] 
      body]]))

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
          (if (:uid cart) 
            [:p (html-link user-route {:id (:uid cart)} "Owner")] 
            [:p "Unknown Owner"])
          (form-to 
            [:put (build-link cart-route cart)]
            (hidden-field "rev" (:_rev cart))
            (hidden-field "uid" (:uid cart))
            "Description: "
            (text-field "description" (:description cart))
            [:br]            
            (submit-button "Update this Cart"))
          (form-to 
            [:delete (build-link cart-route cart)]
            (hidden-field "rev" (:_rev cart))
            (submit-button "Delete this Cart")))))

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