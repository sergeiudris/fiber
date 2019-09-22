(ns ui.dbquery.view
  (:require  [reagent.core :as r]
             [re-frame.core :as rf]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [ui.dbquery.events]
             [ui.dbquery.subs]
             [ui.dbquery.spec]
             [fiber.spec :as rs]
             [ui.dbquery.core]
             #_["ace-builds/src-noconflict/ace.js" :default Ace]
             ["react-ace/lib/index.js" :default ReactAce]
             ["brace" :as brace]
             ["brace/mode/clojure.js"]
             ["brace/mode/graphqlschema.js"]
             ["brace/mode/json.js"]
             ["brace/theme/github.js"]
             #_["antd/lib/button" :default ant-Button]
             #_["antd/lib/table" :default AntTable]))


(def react-ace (r/adapt-react-class ReactAce))

#_@ui.dbquery.core/editor
#_(js/console.log @ui.dbquery.core/editor)
#_(.getValue @ui.dbquery.core/editor)
#_(with-out-str (cljs.pprint/pprint '(foo bar)))


#_(def ant-table (r/adapt-react-class AntTable))


(comment

  ;
  )

(def editor-default-value
  (with-out-str (cljs.pprint/pprint (ui.dbquery.spec/gen-resource))))

(defn editor-ent
  []
  (let [default-value editor-default-value
        #_(with-out-str (cljs.pprint/pprint (rs/gen-ent)))
        value (r/atom default-value)
        generated-ent (rf/subscribe [:ui.dbquery.subs/generate])]
    (fn []
      [react-ace {:name "respace-ent-editor"
                  :mode "clojure"
                  :theme "github"
                  :className "respace-ent-editor"
                    ;  :default-value default-value
                  :value @value
                  :on-load (fn [edr] (reset! ui.dbquery.core/editor edr))
                  :on-change (fn [val evt] (do
                                             #_(js/console.log val)
                                             (reset! value val)
                                             #_(rf/dispatch [:ui.dbquery.events/editor-val val])))
                  :editor-props {"$blockScrolling" true}}]
      )
    )
  )

#_(def query-editor-default-value
  (with-out-str
    (cljs.pprint/pprint
     '[:find [?a ...] :where [_ :respace.spec/uuid ?a]])))

(def query-editor-default-value
  (with-out-str
    (cljs.pprint/pprint
     '[:find ?e ?uuid
       :where
       [?e :respace.spec/uuid ?uuid]])))



#_(cljs.reader/read-string query-editor-default-value)
#_(cljs.reader/read-string (str '{:x 3}))
#_(cljs.reader/read-string (.getValue @ui.dbquery.core/editor-query))

(defn editor-query
  []
  (let [default-value query-editor-default-value
        value (r/atom default-value)]
    (fn []
      [react-ace {:name "respace-ent-query-editor"
                  :mode "clojure"
                  :theme "github"
                  :className "respace-ent-query-editor"
                    ;  :default-value default-value
                  :value @value
                  :on-load (fn [edr] (reset! ui.dbquery.core/editor-query edr))
                  :on-change (fn [val evt] (do
                                             #_(js/console.log val)
                                             (reset! value val)
                                             #_(rf/dispatch [:ui.dbquery.events/editor-val val])))
                  :editor-props {"$blockScrolling" true}}])))

(def columns [{:title "attr1" :data-index "attr1" :key "attr1"}
              {:title "attr2" :data-index "attr2" :key "attr2"}
              {:title "attr3" :data-index "attr3" :key "attr3"}])

#_(defn query-results-table
  []
  (let []
    (fn []
      [ant-table {:size "small"
                  :columns columns :data []}])))



(defn query-results-editor
  []
  (let [default-value "{}"
        value (rf/subscribe [:ui.dbquery.subs/query-resp])
        ]
    (fn []
      [react-ace {:name "respace-ent-query-results-editor"
                  :mode "clojure"
                  :theme "github"
                  :className "respace-ent-query-results-editor"
                    ;  :default-value default-value
                  :value (with-out-str (cljs.pprint/pprint @value))
                  :on-load (fn [edr] (reset! ui.dbquery.core/editor-query-results edr))
                  :on-change (fn [val evt] (do
                                             (reset! value val)
                                             ))
                  :editor-props {"$blockScrolling" ##Inf}}])))

(defn cred-panel []
  (let [default-value (with-out-str (cljs.pprint/pprint (rs/gen-ent)))
        value (r/atom default-value)
        create-date (rf/subscribe [:ui.dbquery.subs/create])
        create-resp (rf/subscribe [:ui.dbquery.subs/create-resp])
        ]
    #_(prn default-value)
    (fn []
      (let []
        [:div
         [:span "edit entity:"]
         [:section {:style {:display "flex"}}
          [editor-ent]
          [:div 
           [:div (str "last ::create event: " @create-date)]
           [:div (str "last :ui.dbquery/create-resp: " @create-resp)]]]
         [:br]
         [:span "query entities:"]
         [:section
          [editor-query]
          ]
         [:br]
         [:span "query results:"]
         [query-results-editor]
         ;
         ]
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
