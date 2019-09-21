(ns app.api.v1.usda
  (:require [io.pedestal.http.route :as route]
            [ring.util.response :as ring-resp]
            [tools.core :refer [version prn-members]]
            [clj-time.core :as t]
            [app.db.core :as db]
            [clj-time.format :as f]))


(defn get-ping
  [req]
  (ring-resp/response
   (str {:data "pong"})))






