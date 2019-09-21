(ns app.db.core
  (:require [datomic.api :as d]
            [tools.datomic.core :as td]))


(def db-uri "datomic:free://datomicdb:4334/fiber?password=datomic")

(def _ (d/create-database db-uri))

#_(d/delete-database db-uri)

(def conn (d/connect db-uri))
; (def conn (d/connect client {:db-name "hello"}))

(def db (d/db conn))

(defn db-now []  (d/db conn))

#_(def schema-tx (read-string (slurp "src/app/db/schema.edn")))

#_(do @(d/transact conn schema-tx))


(defn query
  [q-data]
  (let [q-res (d/q q-data (db-now))]
    (cond
      (= (type q-res) java.util.HashSet) (vec q-res)
      :else q-res)))

(defn transact!
  [data]
  @(d/transact conn data))

(comment

  (td/q-attrs db)


  ;
  )