(ns ui.count.view
  (:require  [reagent.core :as r]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [re-frame.core :as rf]
             [ui.count.subs :as subs]
             [ui.count.events :as events]
             ["antd/lib/button" :default AntButton]
             ["antd/lib/input/Search" :default AntInputSearch]
             ["antd/lib/table" :default AntTable]
             ))


(def ant-input-search (r/adapt-react-class AntInputSearch))
(def ant-table (r/adapt-react-class AntTable))


(comment
  
  ;
  )

(defn stateful-comp
  []
  (let [component-state (r/atom {:count 0})]
    (fn []
      [:div ;; That returns hiccup
       [:p "count is: " (get @component-state :count)]
       [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]])))

(def columns [{:title "attr1" :data-index "attr1" :key "attr1"}
              {:title "attr2" :data-index "attr2" :key "attr2"}
              {:title "attr3" :data-index "attr3" :key "attr3"}])

(defn table
    []
    (let []
      (fn []
        [ant-table {:size "small"
                    :columns columns 
                    :data []}])))

(defn search
  []
  (let []
    (fn []
      [ant-input-search {:style {:width "50%"}}])))

(defn count-panel
  []
  (let [module-count @(rf/subscribe [::subs/module-count])
        base-url @(rf/subscribe [:ui.subs/base-url])]
    (fn []
      [:section
       [search]
       [:br]
       [:br]
       [:br]
       [table]])))

