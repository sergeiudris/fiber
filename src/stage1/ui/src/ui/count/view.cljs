(ns ui.count.view
  (:require  [reagent.core :as r]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [re-frame.core :as rf]
             [ui.count.subs :as subs]
             [ui.count.events :as events]
             [ajax.core :refer [GET POST]]
             ["antd/lib/icon" :default AntIcon]
             ["antd/lib/button" :default AntButton]
             ["antd/lib/button/button-group" :default AntButtonGroup]
             ["antd/lib/input" :default AntInput]
             ["antd/lib/input/Search" :default AntInputSearch]
             ["antd/lib/table" :default AntTable]
             ["antd/lib/auto-complete" :default AntAutoComplete]))

(def ant-icon (r/adapt-react-class AntIcon))
(def ant-button (r/adapt-react-class AntButton))
(def ant-button-group (r/adapt-react-class AntButtonGroup))
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
    (let [search-res (rf/subscribe [:ui.count.subs/search-res])
          results-visible? (rf/subscribe [:ui.count.subs/results-visible?])]
      (fn []
        (let [items (:data @search-res)]
          (js/console.log (count items))
          (if @results-visible?
            [ant-table {:size "small"
                        :columns columns
                        :data []}]
            nil))
        )))

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
  [{:keys [on-click]}]
  [ant-button
   {:class "search-btn"
    :style {:margin-right "-12px"}
    :size "default"
    :on-click on-click
    :type "default"}
   [ant-icon {:type "search"}]])

(defn auto-complete
  [{:keys []}]
  (let [state (r/atom {:input ""})
        on-select (fn [s]
                    (prn "selected " s))
        on-search (fn [s]
                    (rf/dispatch [:ui.count.events/search {:input s}]))
        on-change (fn [s]
                    #_(prn "s:" (.. evt -target -value))
                    (swap! state assoc :input s))
        on-key-up (fn [evt]
                    (when (= (.-key evt) "Enter")
                      (on-search (.. evt -target -value))))]
    (fn [_]
      [ant-auto-complete
       {:style {:width "50%"}
        :size "default"
        :placeholder "search"
        :on-search on-change
        :on-select on-select
        :option-label-prop "text"}
       [ant-input
        {:value (:input @state)
         :on-press-enter on-key-up
        ;  :on-key-up on-key-up
         :suffix (r/as-element [auto-complete-suffix
                                {:on-click #(on-search (:input @state))}])}
        ]
       ])))

(defn buttons
  []
  (let []
    (fn []
      [ant-button-group
       {:size "small"
        :style {:margin-left 8}}
       [ant-button {:icon "unordered-list" :on-click #(rf/dispatch [:ui.count.events/results-visible] ) }]])))

(defn count-panel
  []
  (let [module-count @(rf/subscribe [::subs/module-count])
        base-url @(rf/subscribe [:ui.subs/base-url])]
    (fn []
      [:section
       #_[search]
       [auto-complete {}]
       [buttons]
       [:br]
       [:br]
       [table]])))

