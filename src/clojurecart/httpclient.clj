(ns clojurecart.httpclient
  (:require [clj-http.client :as hc]))

(defn http-get [url]
  (hc/get url {:throw-exceptions false}))

(defn http-put 
  ([url] (hc/put url {:throw-exceptions false}))
  ([url content-type body] (hc/put url {:body body :content-type content-type :throw-exceptions false})))

(defn http-post [url content-type body] 
  (hc/post url {:body body :content-type content-type :throw-exceptions false}))

(defn http-delete [url] 
  (hc/delete url {:throw-exceptions false}))
