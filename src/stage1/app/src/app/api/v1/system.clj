(ns app.api.v1.system
  (:require [io.pedestal.http.route :as route]
            [ring.util.response :as ring-resp]
            [tools.core :refer [version]]
            [clj-time.core :as t]
            [clj-time.format :as f]))


(defn get-system-info
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (version)
                              (route/url-for ::get-system-info))))

(defn now-str
  []
  (f/unparse (f/formatters :basic-date-time) (t/now)))

(defn get-now
  [req]
  (ring-resp/response
   (str {:data (now-str)})))