(ns app.api.v1.core
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [ring.util.response :as ring-resp]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.ring-middlewares :refer [cookies]]
            [tools.core :refer [version]]
            [app.api.v1.system]
            [app.api.v1.query]
            [app.api.v1.usda]))

(def cnt (atom 0))

(defn home-page
  [request]
  (swap! cnt inc)
  (ring-resp/response (str "Hello World! count is: " @cnt)))


;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def itcprs [(body-params/body-params) http/html-body cookies])

;; Tabular routes
(def routes #{["/v1" :get (conj itcprs `home-page)]
              ["/v1/system-info" :get (conj itcprs `app.api.v1.system/get-system-info)]
              ; ["/v1/ents2" :get (conj itcprs `app.api.v1.ent/ents-get)]
              ["/v1/query" :post (conj itcprs `app.api.v1.query/post-query)]
              ["/v1/usda/ping" :get (conj itcprs `app.api.v1.usda/get-ping)]
              ["/v1/usda/search" :get (conj itcprs `app.api.v1.usda/get-search)]
              ["/v1/usda/nutrients" :get (conj itcprs `app.api.v1.usda/get-nutrients)]
              ["/v1/usda/items-nutrients" :post (conj itcprs `app.api.v1.usda/post-items-nutrients)]
              ["/v1/usda/nhi-dri" :get (conj itcprs `app.api.v1.usda/get-nih-dri)]
              ["/v1/usda/example-queries" :get (conj itcprs `app.api.v1.usda/get-example-queries)]
              ;
              })

#_(def routes `{"/v1" {:interceptors common-interceptors
                     :get `home-page
                     "/system-info" {:get `app.api.v1.system/get-system-info}}
              })

;; Map-based routes
;(def routes `{"/" {:interceptors [(body-params/body-params) http/html-body]
;                   :get home-page
;                   "/about" {:get about-page}}})

;; Terse/Vector-based routes
;(def routes
;  `[[["/" {:get home-page}
;      ^:interceptors [(body-params/body-params) http/html-body]
;      ["/about" {:get about-page}]]]])