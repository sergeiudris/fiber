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

(defn stateful-comp
  []
  (let [component-state (r/atom {:count 0})]
    (fn []
      [:div ;; That returns hiccup
       [:p "count is: " (get @component-state :count)]
       [:button {:on-click #(swap! component-state update-in [:count] inc)} "Increment"]])))


(def food-des-keywords
  [
  ;  :usda.item/id
  ;  :usda.item/group-id
   :usda.item/desc-long
  ;  :usda.item/desc-short
  ;  :usda.item/com-name
  ;  :usda.item/manufac-name
  ;  :usda.item/survey
  ;  :usda.item/ref-desc
  ;  :usda.item/refuse
  ;  :usda.item/sci-name
  ;  :usda.item/n-factor
  ;  :usda.item/pro-factor
  ;  :usda.item/fat-factor
  ;  :usda.item/cho-factor
   ]
  )


(def food-des-columns
  (mapv (fn [kw]
          {:title (name kw)
           :key (name kw)
           :dataIndex kw
          ;  :render (fn [txt rec idx]
          ;            txt)
           })
        food-des-keywords))

(def food-des-key :usda.item/id)

(def extra-columns
  [{:title "action"
    :key "action"
    :render (fn [txt rec idx]
              (r/as-element
               [ant-button-group
                {:size "small"}
                [ant-button
                 {;:icon "plus"
                  :type "primary"
                  :on-click #(rf/dispatch 
                              [:ui.count.events/add-items
                               [{:db/id (aget rec "id")}]
                               ])}
                 "add"
                 ]])
              )}])

(def columns (vec (concat food-des-columns extra-columns))) 

#_(def pagination {:show-size-changer true
                 :default-page-size 10
                 :page-size-options ["5" "10" "20"]
                 :position "top"
                 :show-total #(str "Total: " % " entities")})
(defn table
    []
    (let [search-res (rf/subscribe [:ui.count.subs/search-res])
          results-visible? (rf/subscribe [:ui.count.subs/results-visible?])
          table-mdata (rf/subscribe [:ui.count.subs/search-table-mdata])
          ]
      (fn []
        (let [items (:data @search-res)
              total (:total @search-res)
              ents 
              items
              #_(mapv #(-> % :entity (dissoc :db/id)) items)
              pagination (:pagination @table-mdata)]
          (if @results-visible?
            [ant-table {:show-header false
                        :size "small"
                        :row-key food-des-key
                        :columns columns
                        :dataSource ents
                        :on-change (fn [pag fil sor ext]
                                     (rf/dispatch [:ui.count.events/search-table-mdata
                                                   (js->clj {:pagination pag
                                                             :filters fil
                                                             :sorter sor
                                                             :extra ext} :keywordize-keys true)]))
                        :scroll {
                                ;  :x "max-content" 
                                ;  :y 256
                                 }
                        ; :rowSelection {:on-change (fn [keys rows]
                        ;                             (prn keys)
                        ;                             )}
                        :pagination (merge pagination
                                           {:total total
                                            ; :on-change #(js/console.log %1 %2)
                                            })}]
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


(def table-items-keywords
  [
  ;  :usda.item/id
  ;  :usda.item/group-id
   :usda.item/desc-long
  ;  :usda.item/desc-short
  ;  :usda.item/com-name
  ;  :usda.item/manufac-name
  ;  :usda.item/survey
  ;  :usda.item/ref-desc
  ;  :usda.item/refuse
  ;  :usda.item/sci-name
  ;  :usda.item/n-factor
  ;  :usda.item/pro-factor
  ;  :usda.item/fat-factor
  ;  :usda.item/cho-factor
   ])


(def table-items-columns
  (mapv (fn [kw]
          {:title (name kw)
           :key (name kw)
           :dataIndex kw
          ;  :render (fn [txt rec idx]
          ;            txt)
           })
        table-items-keywords))

(def table-items-key :usda.item/id)


(defn table-items
  []
  (let [selected (rf/subscribe [:ui.count.subs/selected-items])]
    (fn []
      (let [items @selected]
        [ant-table {:show-header true
                    :size "small"
                    :row-key (fn [rec idx]
                               (str (aget rec :id) idx))
                    :columns table-items-columns
                    :dataSource items
                    :on-row (fn [rec idx]
                             #js {:onClick (fn [] (js/console.log rec))})
                    :scroll {;  :x "max-content" 
                             :y 364}
                    :pagination false
                    :rowSelection {:on-change (fn [keys rows]
                                                (prn keys))}}]))))

(defn count-panel
  []
  #_(js/console.log 'count-panel-fn)
  (rf/dispatch [:ui.count.events/nutrients])
  (rf/dispatch [:ui.count.events/nhi-dri])
  (let [module-count @(rf/subscribe [::subs/module-count])
        base-url @(rf/subscribe [:ui.subs/base-url])]
    (fn []
      [:section
       #_[search]
       [auto-complete {}]
       [buttons]
       [:br]
       [:br]
       [table]
       [:br]
       [table-items]
       #_[ui.count.sample/sample-table]])))

