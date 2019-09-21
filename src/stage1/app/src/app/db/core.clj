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
#_(do @(d/transact conn schema-tx))


#_(def sample (read-string (slurp "src/app/db/sample.edn")))

#_(def nutr-def (read-string (slurp "/opt/data/stage1/nutr-def.edn")))
#_(do @(d/transact conn nutr-def))

#_(def food-des (read-string (slurp "/opt/data/stage1/food-des.edn")))
#_(def _ (do @(d/transact conn food-des)))



#_(count nutr-def)



(defn query
  [q-data]
  (let [q-res (d/q q-data (db-now))]
    (cond
      (= (type q-res) java.util.HashSet) (vec q-res)
      :else q-res)))

(defn transact!
  [data]
  @(d/transact conn data))

(defn food-des-query
  [s]
  (try
    {:data (d/q '[:find ?entity ?name ?tx ?score
                  :in $ ?search
                  :where [(fulltext $ :usda.item/desc-long ?search) [[?entity ?name ?tx ?score]]]]
                (db-now)
                s)}
    (catch Exception e {:msg (.getMessage e)})))

(defn food-des-search
  [s]
  (let [q-res (food-des-query s)
        q-res-data (:data q-res)]
    (if q-res-data
      {:data (vec
              (map (fn [tup]
                     {:entity nil #_(d/pull (db-now) '[*] (first tup))
                      :db/id (nth tup 0)
                      :usda.item/desc-long (nth tup 1)
                      :tx (nth tup 2)
                      :score (nth tup 3)}) (vec q-res-data)))}
      q-res)))

; https://docs.datomic.com/on-prem/query.html#fulltext





(comment

  (td/q-attrs (db-now))

  (td/q-idents (db-now))
  
  (source line-seq)
  
  (d/q '[:find ?entity ?name ?tx ?score
         :in $ ?search
         :where [(fulltext $ :usda.nutr/desc ?search) [[?entity ?name ?tx ?score]]]]
       (db-now) 
   #_"Tocopherol"
   "Protein"
   #_"Sucrose"
   )
  
  (td/count-total (db-now) :usda.nutr/desc )
  (td/get-paginted-entity
   {:db (db-now)
    :attribute :usda.nutr/desc
    :limit 10
    :offset 0})
  
  (td/count-total (db-now) :usda.item/id)
  (td/get-paginted-entity
   {:db (db-now)
    :attribute :usda.item/id
    :limit 10
    :offset 0})
  
  (d/q '[:find ?entity ?name ?tx ?score
         :in $ ?search
         :where [(fulltext $ :usda.item/desc-long ?search) [[?entity ?name ?tx ?score]]]]
       (db-now)
       "B"
       )
  
  (food-des-search "Buckwheat")
  
  (food-des-search "Buck")
  
  
  
  ;
  )