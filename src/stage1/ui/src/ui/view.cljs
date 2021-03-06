(ns ui.view
  (:use-macros [cljs.core.async.macros :only [go]])
  (:require [cljs.repl :as repl]
            [cljs.pprint :as pp]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [shadow.loader :as loader]
            [ui.subs :as subs]
            [ui.count.view]
            [ui.home.view]
            [cljs.core.async :refer [<! timeout]]
            [ui.config :as config]
            [clojure.string]
            [ui.routes]
            [tools.comp.layout :as layout]
            ["antd/lib/menu" :default AntMenu]
            ["antd/lib/icon" :default AntIcon]))

(defn fn-to-call-on-load []
  (js/console.log "module loaded"))

(defn fn-to-call-on-error []
  (js/console.log "module load failed"))


(defn not-found-panel []
  [:div
   [:span "not found :("]])

; @(resolve 'clojure.repl/dir)  wrong , macro
; ((resolve 'clojure.core/first) [1 2]) works

(defn- resolve-module
  [module-name]
  (case module-name
    "count" {:panel [(resolve 'ui.count.view/count-panel)]
             :actions nil}
    "home" {:panel [(resolve 'ui.home.view/home-panel)]
            :actions nil}
    "dbquery" {:panel [(resolve 'ui.dbquery.view/panel)]}
    "settings" {:panel [(resolve 'ui.settings.view/settings-panel)]}
    "monitor" {:panel [(resolve 'ui.monitor.view/monitor-panel)]}
    [:div (str "no panel for module: " module-name)]))

(defn module->panel
  [module-name]
  (->
   (resolve-module module-name)
   (:panel)))

(defn module->actions
  [module-name]
  (or (->
       (resolve-module module-name)
       (:actions)) (fn [] nil)))

(defn panel->module-name
  [panel-name]
  (if panel-name
    (-> (name panel-name) (clojure.string/split #"-") first)
    nil))

(defn panel-defered
  [module-name]
  (let [comp-state (r/atom {})]
    (fn [module-name]
      (let [panel (@comp-state module-name)]
        ; (prn module-name)
        ; (prn panel)
        (cond
          (loader/loaded? module-name) (module->panel module-name)
          panel [panel]
          :else
          (do
            (go
              (<! (timeout (if config/debug? 100 0)))
              (-> (loader/load module-name)
                  (.then
                   (fn []
                     (rf/dispatch [:ui.events/inc-module-count])
                     (swap! comp-state update-in [module-name] (module->panel module-name)))
                   (fn [] (js/console.log (str "module load failed: " module-name))))))
            #_[:div "nothing"]
            [:div "loading..."])
          ;
          )))))

#_(defn- panels [panel-name]
    (case panel-name
      :count-panel [count-panel]
      :query-panel [query-panel]
      :cred-panel
      #_[cred-panel-defered "cred"] ; 506kb main.js 123kb cred.js
      [cred-panel] ; 624kb
      :entity-panel [entity-panel]
      :timeline-panel [timeline-panel]
      [not-found-panel]))

(defn- panels [panel-name]
  (case panel-name
    :count-panel [ui.count.view/count-panel]
    :home-panel [ui.home.view/home-panel]
    :dbquery-panel [panel-defered "dbquery"]
    :settings-panel [panel-defered "settings"]
    :monitor-panel [panel-defered "monitor"]
    [:div (str "no panel: " panel-name)]))


(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (rf/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))

(defn act-panel-con
  []
  (let [active-panel (rf/subscribe [::subs/active-panel])
        module-count @(rf/subscribe [::subs/module-count]) ; triggers render
        module-name (panel->module-name @active-panel)
        actions-fn (module->actions module-name)
        ]
    #_(prn "act-panel-con: " @active-panel)
    #_(prn "act-panel-con: " @module-count)
    #_(prn (actions-fn))
    #_[act-panel {:module-actions  (actions-fn) }]
    )
  )
; (keyword (str (name (:handler matched-route)) "-panel"))

(def ant-menu (r/adapt-react-class AntMenu))
(def ant-menu-item (r/adapt-react-class (.-Item AntMenu)))
(def ant-icon (r/adapt-react-class AntIcon))

(defn sidebar-menu
  []
  (let [on-select (fn [eargs]
                    (let [eargs-clj (js->clj eargs :keywordize-keys true)
                          {:keys [key]} eargs-clj]
                      (ui.routes/set-path! (str "/" (panel->module-name (keyword key) )) )
                      #_(rf/dispatch [:ui.events/set-active-panel (keyword key)])))
        active-panel (rf/subscribe [:ui.subs/active-panel])
        ]
    (fn []
      [ant-menu {:theme "light"
                 :mode "inline"
                 :default-selected-keys ["count-panel"]
                 :selected-keys (if @active-panel [(name @active-panel)] nil)
                 :on-select on-select}
       [ant-menu-item {:key "count-panel"}
        [ant-icon {:type "pie-chart"}]
        [:span "count"]]
       [ant-menu-item {:key "dbquery-panel"}
        [ant-icon {:type "database"}]
        [:span "dbquery"]]
       [ant-menu-item {:key "settings-panel"}
        [ant-icon {:type "setting"}]
        [:span "settings"]]
       #_[ant-menu-item {:key "monitor-panel"}
        [ant-icon {:type "monitor"}]
        [:span "monitor"]]
       ])))

(defn ui
  []
  [layout/ant-layout-sider-2col
   [sidebar-menu]
   [main-panel]
   ]
  #_[main-panel])