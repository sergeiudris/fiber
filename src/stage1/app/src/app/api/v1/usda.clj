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

(defn get-search
  [req]
  (let [{:keys [body params json-params headers edn-params]} req
        s (:s params)
        data {:data (if s (db/food-des-search s) [] ) } ]
    (ring-resp/response
     (str {:data data})))
  )

#_(try (db/transact! [(:user edn-params)])
     (catch Exception e {:err-msg (.getMessage e)}))

