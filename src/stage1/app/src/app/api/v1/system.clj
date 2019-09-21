(ns app.api.v1.system
  (:require [io.pedestal.http.route :as route]
            [ring.util.response :as ring-resp]
            [tools.core :refer [version]]))


(defn get-system-info
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (version)
                              (route/url-for ::get-system-info))))
