(ns app.setup
  (:require [clojure.repl :refer :all]
            [app.db.core :as db]
            [datomic.api :as d]
            [app.db.query :as dbq]
            [app.data.usda]))


(defn db-populated?
  []
  (try
    (and
     (= 150 (ffirst (d/q (:q dbq/count-nutrients) (db/db-now))))
     (= 8789 (ffirst (d/q (:q dbq/count-food-items) (db/db-now)))))
    (catch Exception e (do (prn (.getMessage e)) false))))

#_(db-populated?)

(defn init!
  []
  (app.data.usda/create-files!)
  (app.db.core/populate!))