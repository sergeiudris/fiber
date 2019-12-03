(ns ui.settings.view
  (:require  [reagent.core :as r]
             [re-frame.core :as rf]
             [cljs.repl :as repl]
             [cljs.pprint :as pp]
             [ui.settings.events]
             [ui.settings.subs]
             [ui.settings.spec]
             [ui.settings.core]
             ["antd/lib/row" :default AntRow]
             ["antd/lib/col" :default AntCol]
             ["antd/lib/select" :default AntSelect]
   
             #_["antd/lib/button" :default ant-Button]
             #_["antd/lib/table" :default AntTable]))


(def ant-row (r/adapt-react-class AntRow ))
(def ant-col (r/adapt-react-class AntCol))
(def ant-select (r/adapt-react-class AntSelect))
(def ant-select-option (r/adapt-react-class (.-Option AntSelect)))


(comment

  ;
  )

(defn settings-panel []
  (let []
    (fn []
      (let []
        [:section
         [:div "settings"]
         [:br]
         [ant-row
          [ant-col {:span 3} "Age group: "]
          [ant-col {:span 4}
           [ant-select {:default-value "31-50"
                        :style {:width "120px"}
                        :on-change (fn [vl] (js/console.log vl))}
            [ant-select-option {:value "31-50"} "31-50"]]]
          ]
         ;
         ]
        ;
        ))))

(defn module-actions []
  [])
