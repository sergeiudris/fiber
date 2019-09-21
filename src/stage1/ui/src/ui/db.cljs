(ns ui.db
  (:require [clojure.spec.alpha :as s])
  )

(defn gen-default-conf
  "Returns the default api conf"
  []
  (let [api-v1-baseurl "http://localhost:5600/v1"]
    {:api.v1/base-url api-v1-baseurl}))

(defn gen-default-db
  "gens the deafult db"
  []
  (let [conf (gen-default-conf)
        base-url (:api.v1/base-url conf)]
    {; core

     :ui.core/name "re-frame"
     :ui.core/count 0
     :ui.core/module-count 0
     :ui.core/active-panel nil
     :ui.core/conf conf
     :ui.core/api {:base-url base-url
                   :ents (str base-url "/ents")
                   :ent (str base-url "/ent")}

    ; cred

    ;
     }))

(def default-db (gen-default-db))

#_(get-in default-db [:ui.core/api :base-url] )