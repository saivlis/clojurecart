(ns clojurecart.httpclient
  (:require [clj-http.client :as hc]))

(defn http-get [url]
  (hc/get url {:throw-exceptions false}))

(defn http-put 
  ([url] (hc/put url {:throw-exceptions false}))
  ([url body] (hc/put url body {:throw-exceptions false})))