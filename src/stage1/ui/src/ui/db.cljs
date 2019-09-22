(ns ui.db
  (:require [clojure.spec.alpha :as s])
  )

(defn gen-default-conf
  "Returns the default api conf"
  []
  (let [api-v1-baseurl "http://localhost:4500/v1"]
    {:api.v1/base-url api-v1-baseurl}))

(defn gen-default-db
  "gens the deafult db"
  []
  (let [conf (gen-default-conf)
        base-url (:api.v1/base-url conf)]
    {; core

     :ui.core/name "fiber"
     :ui.core/count 0
     :ui.core/module-count 0
     :ui.core/active-panel nil
     :ui.core/conf conf
     :ui.core/api {:base-url base-url
                   :search (str base-url "/usda/search")}

    ; count

     :ui.count/search-res {:data []}
     :ui.count/results-visible? true
     :ui.count/search-table-mdata {:pagination {:showSizeChanger true
                                                :defaultPageSize 10
                                                :pageSizeOptions  ["5" "10" "20"]
                                                :position "top"
                                                :total 0
                                                :current 1
                                                :pageSize 10}
                                   :filters {}
                                   :sorter {}
                                   :extra {:currentDataSource []}}
     :ui.count/search-input ""

    ;
     }))

(def default-db (gen-default-db))

#_(get-in default-db [:ui.core/api :base-url] )