(ns app.db.core
  (:require   [clojure.repl  :refer :all]
              [datomic.api :as d]
              [tools.datomic.core :as td]
              [tools.io.core :as tio]
              [clojure.java.io :as io]
              [clojure.pprint :as pp]))


(def db-uri "datomic:free://db:4334/fiber?password=datomic")

#_(d/delete-database db-uri)

(declare conn)

(defn db-now []  (d/db conn))

(defn connect!
  []
  (try
    (do
      (d/create-database db-uri)
      (def  conn (d/connect db-uri)))
    (catch Exception e (do (prn (.getMessage e)) false))))

#_(connect!)

(defn db-exists?
  []
  (try
    (def conn (d/connect db-uri))
    (catch Exception e (do (prn (.getMessage e)) false))))

#_(db-exists?)

; (def conn (d/connect client {:db-name "hello"}))

#_(def schema-tx (read-string (slurp "src/app/db/schema.edn")))
#_(do @(d/transact conn schema-tx))


#_(def sample (read-string (slurp "src/app/db/sample.edn")))

#_(def nutr-def (read-string (slurp "/opt/data/stage1/nutr-def.edn")))
#_(do @(d/transact conn nutr-def))

#_(def food-des (read-string (slurp "/opt/data/stage1/food-des.edn")))
#_(def _ (do @(d/transact conn food-des)))

#_(def nih-dri-elements (read-string (slurp "/opt/data/stage1/nih.dri.elements.edn")))
#_(def _ (do @(d/transact conn nih-dri-elements)))

#_(count nutr-def)

#_(td/excise conn [17592186733493 17592186733477 17592186054375 17592186054375] )

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
              (prn "transacted: " (* @tx-count line-num)))
               ;
          )))))

#_(time (batch-tx "/opt/data/stage1/nut-data.edn" conn))
#_(.exists (io/file "/opt/data/stage1/nut-data.edn"))
#_(tio/count-lines "/opt/data/stage1/nut-data.edn")
#_(td/count-total (db-now) :usda.nutrdata/id)

(defn populate!
  []
  (try
    (do
      (time @(d/transact conn (read-string (slurp "src/app/db/schema.edn"))))
      (time @(d/transact conn (read-string (slurp "/opt/data/stage1/nutr-def.edn"))))
      (time @(d/transact conn (read-string (slurp "/opt/data/stage1/food-des.edn"))))
      (time @(d/transact conn (read-string (slurp "/opt/data/stage1/nih.dri.elements.edn"))))
      (time (batch-tx "/opt/data/stage1/nut-data.edn" conn))
      ;
      )
    (catch Exception e (do (prn (.getMessage e)) false))))

#_(populate!)

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
  [attr s & {:keys [offset limit entity?]  }]
  (let [q-res (query-fulltext attr s)
        q-res-data (:data q-res)]
    (if q-res-data
      {:total (count q-res-data)
       :search s
       :data (->>
              (drop offset q-res-data)
              (take limit)
              (map (fn [tup]
                     {:entity (when entity? (d/pull (db-now) '[*] (first tup)))
                      #_(d/pull (db-now) '[:usda.item/desc-long] (first tup))
                      #_(d/pull (db-now) '[*] (first tup))
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

(def nutrient-search (mk-search-fulltext :usda.nutr/desc))

(def nutrients-q
  '[:find ?e
    :in $
    :where [?e :usda.nutr/id]])

(defn query-nutrients
  []
  (try
    (let [qres (d/q nutrients-q
                    (db-now))]
      {:total (count qres)
       :data (->>
              (map (fn [v]
                     (-> (db-now) (d/entity (first v)) d/touch)) qres)
              (vec))})
    (catch Exception e {:msg (.getMessage e)})))

(defn query-items-nutrients
  [eids]
  (try
    (let [qres (map (fn [e]
                      (d/pull (db-now) '[*] e)) eids)]
      {:total (count qres)
       :data (vec qres)})
    (catch Exception e {:msg (.getMessage e)})))

(defn qpull-entities
  [attr value]
  (try
    (let [qres (d/q '{:find [?e]
                      :in [$ ?attr ?val]
                      :where [[?e ?attr ?val]]}
                    (db-now) attr value)]
      {:total (count qres)
       :data (->>
              (map (fn [v]
                     (-> (db-now) (d/entity (first v)) d/touch)) qres)
              (vec))})
    (catch Exception e {:msg (.getMessage e)})))

(defn query-nih-dri
  [group-range]
  (qpull-entities :nih.dri.group/range group-range))

; https://docs.datomic.com/on-prem/query.html#fulltext

(comment

  (query-nutrients)
  
  (qpull-entities :nih.dri.group/range "0-0.5")
  
  (query-nih-dri "31-50")

  (pp/pprint (food-des-search
              "Beans"
              :offset 0
              :limit 2))

  (query-items-nutrients [17592186047001 17592186050667])

  (def _ (food-des-search
          "Beans"
          :offset 0
          :limit 20))

  (food-des-search
   "Beans"
   :offset nil
   :limit nil)

  ; find all dinstinct units
  (def units (d/q '{:find [(distinct ?unit)]
                    :in [$]
                    :where [[?e :usda.nutr/units ?unit]]}
                  (db-now)))
  
  (= (second (ffirst units)) "?g")
  (first (second (ffirst units))) ; \�
  
  (nutrient-search "Vitamin+K" :offset 0 :limit 1 :entity? true)
  
  (pp/pprint (search-fulltext :usda.nutr/desc "Choline"
                              :offset 0 :limit 50 :entity? true))

  
  
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
  
  (def nih-dri (d/q '{:find [?e]
                    :in [$]
                    :where [[?e :usda.nutr/units ?unit]]}
                  (db-now)))
  
  
  
  ;
  )