(ns clojurecart.mediatypes)

(def html '("text" "html"))
(def json '("application" "json"))
(def urlenc '("application" "x-www-form-urlencoded"))

(defn mediatype-to-s [[mt st]]
  (str mt "/" st))
