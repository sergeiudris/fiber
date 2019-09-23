(ns app.api.v1.usda
  (:require [io.pedestal.http.route :as route]
            [ring.util.response :as ring-resp]
            [tools.core :refer [version prn-members try-parse-int]]
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
        {:keys [s offset limit] :or {offset 0 limit 1}} params
        offset-num  (try-parse-int offset)
        limit-num  (try-parse-int limit)
        data (if s
               (db/food-des-search s :offset offset-num :limit limit-num)
               {})]
    (ring-resp/response
     (str data)))
  )

(defn get-nutrients
  [req]
  (let [{:keys [body params json-params headers edn-params]} req
        data (db/query-nutrients)]
    (ring-resp/response
     (str data))))

(defn post-items-nutrients
  [req]
  (let [{:keys [body params json-params headers edn-params]} req
        items (:items edn-params)
        data (db/query-items-nutrients (mapv #(:db/id %) items))]
    (ring-resp/response
     (str data))))

#_(try (db/transact! [(:user edn-params)])
     (catch Exception e {:err-msg (.getMessage e)}))

