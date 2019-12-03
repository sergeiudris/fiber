(ns ui.dbquery.view
  (:require  [reagent.core :as r]
             [re-frame.core :as rf]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [ui.dbquery.events]
             [ui.dbquery.subs]
             [ui.dbquery.spec]
             [ui.dbquery.core]
             #_["ace-builds/src-noconflict/ace.js" :default Ace]
             ["react-ace/lib/index.js" :default ReactAce]
             ["brace" :as brace]
             ["brace/mode/clojure.js"]
             ["brace/mode/graphqlschema.js"]
             ["brace/mode/json.js"]
             ["brace/theme/github.js"]
             ["antd/lib/icon" :default AntIcon]
             ["antd/lib/button" :default AntButton]
             ["antd/lib/button/button-group" :default AntButtonGroup]
             #_["antd/lib/button" :default ant-Button]
             ["antd/lib/table" :default AntTable]))


(def react-ace (r/adapt-react-class ReactAce))
(def ant-icon (r/adapt-react-class AntIcon))
(def ant-button (r/adapt-react-class AntButton))
(def ant-button-group (r/adapt-react-class AntButtonGroup))
(def ant-table (r/adapt-react-class AntTable))

#_@ui.dbquery.core/editor
#_(js/console.log @ui.dbquery.core/editor)
#_(.getValue @ui.dbquery.core/editor)
#_(with-out-str (cljs.pprint/pprint '(foo bar)))



(def query-editor-default-value
  (with-out-str
    (cljs.pprint/pprint
     '[:find (count ?e)
       :where
       [?e :usda.nutr/id]])))

(defn editor-query
  []
  (let [default-value ""
        value (r/atom default-value)]
    (fn []
      [react-ace {:name "fiber-db-query-editor"
                  :mode "clojure"
                  :theme "github"
                  :className "fiber-db-query-editor"
                    ;  :default-value default-value
                  :value @value
                  :on-load (fn [edr] (reset! ui.dbquery.core/editor-query edr))
                  :on-change (fn [val evt] (do
                                             #_(js/console.log val)
                                             (reset! value val)
                                             #_(rf/dispatch [:ui.dbquery.events/editor-val val])))
                  :editor-props {"$blockScrolling" true}}])))

(defn editor-results
  []
  (let [default-value "{}"
        value (rf/subscribe [:ui.dbquery.subs/query-resp])
        ]
    (fn []
      [react-ace {:name "fiber-db-query-results-editor"
                  :mode "clojure"
                  :theme "github"
                  :className "fiber-db-query-results-editor"
                    ;  :default-value default-value
                  :value (with-out-str (cljs.pprint/pprint @value))
                  :on-load (fn [edr] (reset! ui.dbquery.core/editor-query-results edr))
                  :on-change (fn [val evt] (do
                                             (reset! value val)
                                             ))
                  :editor-props {"$blockScrolling" ##Inf}}])))

(defn buttons
  []
  (let []
    (fn []
      [ant-button-group
       {:size "small"}
       [ant-button
        {:on-click (fn [] (rf/dispatch [:ui.dbquery.events/query]))}
        "query"]]
      )
    )
  )

(defn example-queries-list
  []
  (let [data (rf/subscribe [:ui.dbquery.subs/example-queries])]
    (fn []
      (let [qs @data]
        [:section
         (map (fn [q]
                [:div {:key (:name q)}
                 (:name q)]) qs)]))))

(def example-queries-columns
  [{:title "name"
    :key "name"
    :dataIndex :name
     }]
  )

(defn example-queries-table
  []
  (let [data (rf/subscribe [:ui.dbquery.subs/example-queries])]
    (fn []
      (let [qs @data]
        [ant-table
         {:showHeader false
          :columns example-queries-columns
          :dataSource qs
          :size "small"
          :rowClassName "fiber-example-queries-table-row"
          :row-key (fn [rec idx]
                     (aget rec "name"))
          :onRow (fn [rec idx]
                   #js {:onClick (fn []
                                   (rf/dispatch
                                    [:ui.dbquery.events/select-query
                                     rec]))
                        :onDoubleClick (fn [] (js/console.log rec))})
          :pagination false}]))))

(defn panel []
  (rf/dispatch [:ui.dbquery.events/example-queries])
  (let []
    (fn []
      (let []
        [:div {:style {:display "flex"}}
         [:section
          [:span "query"]
          [editor-query]
          [:br]
          [buttons]
          [:br]
          [:br]
          [:span "results"]
          [editor-results]
         ;
          ]
         [:section {:style {:padding-top 20 :padding-left 32}}
          [example-queries-table]]]
        ;
        ))))

(defn module-actions []
  [{:re-frame-key :ui.dbquery.events/generate
      :title "generate"}
   {:re-frame-key :ui.dbquery.events/query
    :title "query"}
   {:re-frame-key :ui.dbquery.events/validate
    :title "validate"}
   {:re-frame-key :ui.dbquery.events/create
    :title "cred"}])
