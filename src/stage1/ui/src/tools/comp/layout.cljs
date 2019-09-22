(ns tools.comp.layout
  (:require [cljs.repl :as repl]
            [cljs.pprint :as pp]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string]
            ["antd/lib/layout" :default AntLayout]
            ["antd/lib/layout/Sider" :default AntSider]
            #_["antd/lib/menu" :default AntMenu]
            #_["antd/lib/icon" :default AntIcon]))

(def ant-layout (r/adapt-react-class AntLayout))
(def ant-layout-content (r/adapt-react-class (.-Content AntLayout)))
(def ant-sider (r/adapt-react-class AntSider))
#_(def ant-menu (r/adapt-react-class AntMenu))
#_(def ant-menu-item (r/adapt-react-class (.-Item AntMenu)))
#_(def ant-icon (r/adapt-react-class AntIcon))

(defn ant-layout-sider-2col
  [menu content]
  [ant-layout {:style {:min-height "100vh"}}
   [ant-sider {:collapsible true :theme "light"}
    menu]
   [ant-layout
    [ant-layout-content {:style {:margin "0 16px"}}
     content]]
   ])