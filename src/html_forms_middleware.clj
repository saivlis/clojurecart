(ns html_forms_middleware)

(defn wrap-html-put-delete-forms [app]
  (fn [request]
    (if (and (= :post (:request-method request)) 
             (= "application/x-www-form-urlencoded" (:content-type request))
             (contains? (:params request) :_method))
      (let [methname (:_method (:params request))
            meth (if (= "DELETE" methname) :delete (if (= "PUT" methname) :put))]
        (if meth 
          (app (assoc request :request-method meth))))
      (app request))))