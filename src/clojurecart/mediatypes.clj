(ns clojurecart.mediatypes)

(def html '("text" "html"))
(def json '("application" "json"))

(defn mediatype-to-s [[mt st]]
  (str mt "/" st))
