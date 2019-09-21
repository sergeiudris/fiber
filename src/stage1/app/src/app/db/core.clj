(ns app.db.core
  (:require   [clojure.repl  :refer :all]
              [datomic.api :as d]
              [tools.datomic.core :as td]))


(def db-uri "datomic:free://datomicdb:4334/fiber?password=datomic")

(def _ (d/create-database db-uri))

#_(d/delete-database db-uri)

(def conn (d/connect db-uri))
; (def conn (d/connect client {:db-name "hello"}))

(def db (d/db conn))

(defn db-now []  (d/db conn))

#_(def schema-tx (read-string (slurp "src/app/db/schema.edn")))

#_(def sample (read-string (slurp "src/app/db/sample.edn")))

#_(def nutr-def (read-string (slurp "/opt/data/stage1/nutr-def.edn")))


#_(do @(d/transact conn schema-tx))

#_(count nutr-def)

#_(do @(d/transact conn nutr-def))


(defn query
  [q-data]
  (let [q-res (d/q q-data (db-now))]
    (cond
      (= (type q-res) java.util.HashSet) (vec q-res)
      :else q-res)))

(defn transact!
  [data]
  @(d/transact conn data))

; https://docs.datomic.com/on-prem/query.html#fulltext





(comment

  (td/q-attrs (db-now))

  (td/q-idents (db-now))
  
  (source line-seq)
  
  (d/q '[:find ?entity ?name ?tx ?score
         :in $ ?search
         :where [(fulltext $ :usda.nutr/desc ?search) [[?entity ?name ?tx ?score]]]]
       (db-now) 
   "Protein"
   #_"Sucrose"
   )
  
  (td/count-total (db-now) :usda.nutr/desc )
  
  (td/get-paginted-entity
   {:db (db-now)
    :attribute :usda.nutr/desc
    :limit 10
    :offset 0})
  
  ;
  )