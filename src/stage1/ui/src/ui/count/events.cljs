(ns ui.count.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]])
  )

(rf/reg-event-fx
 ::search
 (fn [{:keys [db]} [_ eargs]]
   (let [input (:ui.count/search-input db)
         s (or (:input eargs) input)
         table-mdata (:ui.count/search-table-mdata db)
         total (get-in db [:ui.count/search-res :total])
         {:keys [current pageSize]} (:pagination table-mdata)
         limit (or pageSize 10)
         offset (or (* pageSize (dec current)) 0)]
     {:dispatch [:ui.events/request
                 {:method :get
                  :params {:s s :limit limit :offset offset}
                  :path "/usda/search"
                  :on-success [::search-res]
                  :on-fail [::search-res]}]
      :db (assoc db :ui.count/search-input s)})))

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

(rf/reg-event-db
 ::search-input
 (fn-traced [db [_ eargs]]
            (let [key :ui.count/search-input
                  value eargs]
              (assoc db key value))))

(rf/reg-event-fx
 ::search-table-mdata
 (fn [{:keys [db]} [_ eargs]]
            (let [key :ui.count/search-table-mdata
                  ]
              {:dispatch [:ui.count.events/search {}]
               :db (assoc db key eargs)})))

(rf/reg-event-fx
 ::nutrients
 (fn [{:keys [db]} [_ eargs]]
   (let [nutrients (:ui.count/nutrients-res db)]
     (if nutrients
       {:db db}
       {:dispatch [:ui.events/request
                   {:method :get
                    :params {}
                    :path "/usda/nutrients"
                    :on-success [::nutrients-res]
                    :on-fail [::nutrients-res]}]
        :db db}))))

(rf/reg-event-db
 ::nutrients-res
 (fn-traced [db [_ val]]
            (assoc db :ui.count/nutrients-res val)))