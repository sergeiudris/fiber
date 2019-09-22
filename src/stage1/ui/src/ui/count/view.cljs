(ns ui.count.view
  (:require  [reagent.core :as r]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [re-frame.core :as rf]
             [ui.count.subs :as subs]
             [ui.count.events :as events]
             ["antd/lib/button" :default ant-Button]
             ))




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

(defn count-panel []
  (let [module-count @(rf/subscribe [::subs/module-count])
        base-url @(rf/subscribe [:ui.subs/base-url] )
        ]
    [:div "count"]))

