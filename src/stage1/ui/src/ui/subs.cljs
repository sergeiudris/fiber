(ns ui.subs
  (:require [re-frame.core :as rf])
  )

(rf/reg-sub
 ::active-panel
 (fn [db _]
   (:ui.core/active-panel db)))

(rf/reg-sub
 ::module-count
 (fn [db _]
   (:ui.core/module-count db)))

(rf/reg-sub
 ::api
 (fn [db _]
   (get-in db [:ui.core/api])))


#_(fn [_ _]  [(rf/subscribe [:active-attribute]) (rf/subscribe [:entities-table-state])] )
#_(fn [[active-attribute entities-table-state] _])

(rf/reg-sub
 ::base-url
 (fn [_ _] [ (rf/subscribe [::api])])
 (fn [[ api] _]
   (get-in api [:base-url])))
