(ns ui.count.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]])
  )

(rf/reg-event-fx
 ::search
 (fn-traced [{:keys [db]} [_ eargs]]
            (let [s (:input eargs)
                  ]
              {:dispatch [:ui.events/request
                          {:method :get
                           :params {:s s}
                           :path "/usda/search"
                           :on-success [::search-res]
                           :on-fail [::search-res]}]
               :db (assoc db :ui.count/search-ts (js/Date.now))})))

(rf/reg-event-db
 ::search-res
 (fn-traced [db [_ val]]
            (assoc db :ui.count/search-res val)))

(rf/reg-event-db
 ::results-visible
 (fn-traced [db [_ eargs]]
            (let [key :ui.count/results-visible?
                  value (key db)]
              (assoc db key (not value)))))