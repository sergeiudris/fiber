(ns app.db.core
  (:require   [clojure.repl  :refer :all]
              [datomic.api :as d]
              [tools.datomic.core :as td]
              [tools.io.core :as tio]
              [clojure.java.io :as io]
              [clojure.pprint :as pp]))


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

(defn batch-tx
  [file-in  conn & {:keys [line-num] :or {line-num 10000}}]
  (with-open [reader (io/reader  file-in)]
    (let [total-lines (tio/count-lines file-in)
          tx-count (atom 0)
          raw (line-seq reader)
          data (->> raw (drop 1) (drop-last))
          parts (partition-all line-num data)]
      (prn "total lines: " total-lines)
      (doseq [part parts]
        (let [tx-data (map (fn [line]
                             (read-string line))
                           part)]
          (do (time @(d/transact conn (vec tx-data)))
              (swap! tx-count inc)
              (prn "transacted: " (* tx-count line-num)))
               ;
          )))))

#_(time (batch-tx "/opt/data/stage1/nut-data.edn" conn))
#_(.exists (io/file "/opt/data/stage1/nut-data.edn"))
#_(tio/count-lines "/opt/data/stage1/nut-data.edn")
#_(td/count-total (db-now) :usda.nutrdata/id)

(defn query
  [q-data]
  (let [q-res (d/q q-data (db-now))]
    (cond
      (= (type q-res) java.util.HashSet) (vec q-res)
      :else q-res)))

(defn transact!
  [data]
  @(d/transact conn data))

(defn query-fulltext
  [attr s]
  (try
    {:data (d/q '[:find ?entity ?val ?tx ?score
                  :in $ ?attr ?search 
                  :where [(fulltext $ ?attr ?search) [[?entity ?val ?tx ?score]]]]
                (db-now) attr s )}
    (catch Exception e {:msg (.getMessage e)})))

(defn search-fulltext
  [attr s & {:keys [offset limit]  }]
  (let [q-res (query-fulltext attr s)
        q-res-data (:data q-res)]
    (if q-res-data
      {:total (count q-res-data)
       :data (->>
              (drop offset q-res-data)
              (take limit)
              (map (fn [tup]
                     {:entity (d/pull (db-now) '[*] (first tup))
                      :db/id (nth tup 0)
                      attr (nth tup 1)
                      :tx (nth tup 2)
                      :score (nth tup 3)}) )
              vec)}
      q-res)))

(defn mk-search-fulltext
  [attr]
  (partial search-fulltext attr))

(def food-des-search (mk-search-fulltext :usda.item/desc-long))

; https://docs.datomic.com/on-prem/query.html#fulltext

(comment

  (pp/pprint (food-des-search
              "Beans"
              :offset 0
              :limit 1))
  
  (def _ (food-des-search
          "Beans"
          :offset 0
          :limit 20))

  (food-des-search
   "Beans"
   :offset nil
   :limit nil)

  ;
  )



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