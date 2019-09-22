(ns ui.count.view
  (:require  [reagent.core :as r]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [re-frame.core :as rf]
             [ui.count.subs :as subs]
             [ui.count.events :as events]
             [ajax.core :refer [GET POST]]
             [ui.count.sample]
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

#_(def columns [{:title "attr1" :data-index "attr1" :key "attr1"}
              {:title "attr2" :data-index "attr2" :key "attr2"}
              {:title "attr3" :data-index "attr3" :key "attr3"}])

(def food-des-keywords
  [:usda.item/group-id
   :usda.item/desc-long
   :usda.item/desc-short
   :usda.item/com-name
   :usda.item/manufac-name
   :usda.item/survey
   :usda.item/ref-desc
   :usda.item/refuse
   :usda.item/sci-name
   :usda.item/n-factor
   :usda.item/pro-factor
   :usda.item/fat-factor
   :usda.item/cho-factor]
  )


;; people data
#_(def people-data [{:id 1 :name "Tracey Davidson" :age 43 :address "5512 Pockrus Page Rd"}
             {:id 2 :name "Pierre de Wiles" :age 41 :address "358 Fermat's St"}
             {:id 3 :name "Lydia Weaver" :age 23 :address "1251 Fourth St"}
             {:id 4 :name "Willie Reynolds" :age 26 :address "2984 Beechcrest Rd"}
             {:id 5 :name "Richard Perelman" :age 51 :address "2003 Poincaré Ricci Rd"}
             {:id 6 :name "Srinivasa Ramanujan" :age 32 :address "1729 Taxi Cab St"}
             {:id 7 :name "Zoe Cruz" :age 31 :address "8593 Pine Rd"}
             {:id 8 :name "Adam Turing" :age 41 :address "1936 Automata Lane"}])

#_(defn comparison [data1 data2 field]
  (compare (get (js->clj data1 :keywordize-keys true) field)
           (get (js->clj data2 :keywordize-keys true) field)))


;; we need to use dataIndex instead of data-index, see README.md
#_(def people-columns [{:title "Name" :dataIndex "name" :sorter #(comparison %1 %2 :name)}
              {:title "Age" :dataIndex "age" :sorter #(comparison %1 %2 :age)}
              {:title "Address" :dataIndex "address" :sorter #(comparison %1 %2 :address)}])

#_(def pagination {:show-size-changer true
                 :default-page-size 10
                 :page-size-options ["5" "10" "20"]
                 :position "top"
                 :show-total #(str "Total: " % " entities")})

(def food-des-columns
  (mapv (fn [kw]
          {:title (name kw)
           :key (name kw)
           :dataIndex (name kw)
          ;  :render (fn [txt rec idx]
          ;            txt)
           })
        food-des-keywords))

(def columns food-des-columns)

(defn table
    []
    (let [search-res (rf/subscribe [:ui.count.subs/search-res])
          results-visible? (rf/subscribe [:ui.count.subs/results-visible?])]
      (fn []
        (let [items (:data @search-res)
              ents (mapv #(-> % :entity (dissoc :db/id) ) items )
              ]
          (if @results-visible?
            [ant-table {:show-header true
                        :size "small"
                        :row-key "key"
                        :columns columns
                        :data-source (clj->js ents) }]
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
       #_[table]
       [ui.count.sample/sample-table]])))

