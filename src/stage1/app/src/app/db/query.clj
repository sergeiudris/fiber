(ns app.db.query
  (:require [clojure.repl :refer :all]
            [datomic.api :as d]
            [clojure.pprint :as pp]
            [app.db.core :refer [db-now]]))

(def count-nutrients
  {:name "How many USDA nutrients ?"
   :doc "Count entites with attribute :usda.nutr/id"
   :q '{:find [(count ?e)]
        :in [$]
        :where [[?e :usda.nutr/id]]}})

#_(d/q (:q total-nutrients) (db-now))

(def count-food-items
  {:name "How many USDA food items ?"
   :doc "Count entites with attribute :usda.item/id"
   :q '{:find [(count ?e)]
        :in [$]
        :where [[?e :usda.item/id]]}})

#_(d/q (:q count-food-items) (db-now))

(def nih-age-groups
  {:name "NIH RDI age groups "
   :doc "Query for :nih.dri.group/range"
   :q '{:find [?name]
        :in [$]
        :where [[_ :nih.dri.group/range ?name]]}})





