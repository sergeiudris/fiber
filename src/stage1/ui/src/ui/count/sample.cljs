(ns ui.count.sample
  (:require [reagent.core :as r]
            [cljs.repl :as repl]
            [cljs.pprint :as pp]
            [re-frame.core :as rf]
            ["antd/lib/table" :default AntTable]))

(def ant-table (r/adapt-react-class AntTable))


(def data-source 
  [{:key "1"
    :name "Mike"
    :age 32
    :address "10 Downing Street"}
   {:key "2"
    :name "John"
    :age 42
    :address "11 Downing Street"}
   ]
  )

(def columns
  [{:title "Name"
    :dataIndex "name"
    :key "name"}
   {:title "Age"
    :dataIndex "age"
    :key "age"}
   {:title "Address"
    :dataIndex "address"
    :key "address"}
   ]
  )

(def data-source-2
  [{"key" "1"
    "name" "Mike"
    "age" 32
    "address" "10 Downing Street"}
   {"key" "2"
    "name" "John"
    "age" 42
    "address" "11 Downing Street"}])

(def columns-2
  [{"title" "Name"
    "dataIndex" "name"
    "key" "name"}
   {"title" "Age"
    "dataIndex" "age"
    "key" "age"}
   {"title" "Address"
    "dataIndex" "address"
    "key" "address"}])

(defn sample-table
  []
  (let []
    (fn []
      [ant-table
       {:dataSource data-source-2
        :columns columns-2
        }])))