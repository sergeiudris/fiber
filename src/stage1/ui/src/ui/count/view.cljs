(ns ui.count.view
  (:require  [reagent.core :as r]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [re-frame.core :as rf]
             [ui.count.subs :as subs]
             [ui.count.events :as events]
             ["antd/lib/icon" :default AntIcon]
             ["antd/lib/button" :default AntButton]
             ["antd/lib/input" :default AntInput]
             ["antd/lib/input/Search" :default AntInputSearch]
             ["antd/lib/table" :default AntTable]
             ["antd/lib/auto-complete" :default AntAutoComplete]))

(def ant-icon (r/adapt-react-class AntIcon))
(def ant-button (r/adapt-react-class AntButton))
(def ant-input (r/adapt-react-class AntInput))
(def ant-input-search (r/adapt-react-class AntInputSearch))
(def ant-auto-complete (r/adapt-react-class AntAutoComplete))
(def ant-auto-complete-option (r/adapt-react-class (.-Option AntAutoComplete)))


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
  (let [on-search (fn [s]
                    (prn s)
                    
                    )]
    (fn []
      [ant-input-search {:style {:width "50%"}
                         :placeholder "search"
                         :on-search on-search
                         }])))

(defn auto-complete-suffix
  []
  (fn []
    [ant-button
     {:class "search-btn"
      :style {:margin-right "-12px"}
      :size "default"
      :type "primary"}
     [ant-icon {:type "search"}]]))

(defn auto-complete
  [{:keys [on-search]}]
  (let [state (r/atom {:count 0})
        ; on-search (fn [s]
        ;             (prn s))
        on-select (fn [s]
                    (prn "selected " s)
                    )
        ]
    (fn [_]
      [ant-auto-complete
       {:style {:width "50%"}
        :size "default"
        
        :placeholder "search"
        ; :on-search on-search
        :on-select on-select
        :option-label-prop "text"
        }
       [ant-input
        {:on-key-up (fn [evt]
                      (when (= (.-key evt) "Enter")
                        (on-search (.. evt -target -value))))
         :suffix (r/as-element [auto-complete-suffix])
         }
        ]
       ])))

(defn count-panel
  []
  (let [module-count @(rf/subscribe [::subs/module-count])
        base-url @(rf/subscribe [:ui.subs/base-url])]
    (fn []
      [:section
       #_[search]
       [auto-complete {:on-search (fn [s] (prn "input: " s)) }]
       [:br]
       [:br]
       [:br]
       [table]])))

