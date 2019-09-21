(ns app.api.v1.query
  (:require [io.pedestal.http.route :as route]
            [ring.util.response :as ring-resp]
            [tools.core :refer [version prn-members]]
            [app.db.core :as db]
   ))



(defn post-query
  [req]
  (let [{:keys [body params json-params headers edn-params]} req
        q-data (:q-data edn-params)
        data (try (db/query q-data)
                  (catch Exception e {:err-msg (.getMessage e)}))
        #_(try
            (db/query-edn {:qstring qstring})
            (catch Exception e {:err-msg (.getMessage e)}))]
    (ring-resp/response
     data
     ;
     )))
