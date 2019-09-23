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
         pag (:pagination table-mdata)
         {:keys [current pageSize]} pag
         limit (or pageSize 10)
         offset (or (* pageSize (dec current)) 0)]
     {:dispatch [:ui.events/request
                 {:method :get
                  :params {:s s :limit limit :offset offset}
                  :path "/usda/search"
                  :on-success [::search-res]
                  :on-fail [::search-res]}]
      :db (merge db {:ui.count/search-input s
                     :ui.count/search-table-mdata
                     (if (:input eargs)
                       (merge table-mdata {:pagination (merge pag {:current 1})})
                       table-mdata)
                     :ui.count/results-visible? true})})))

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
 (fn-traced [db [_ eargs]]
            (assoc db :ui.count/nutrients-res eargs)))


(rf/reg-event-fx
 ::items-nutrients
 (fn [{:keys [db]} [_ eargs]]
   (let [nutrients (:ui.count/nutrients-res db)]
     {:dispatch [:ui.events/request
                 {:method :post
                  :params {}
                  :body {:items eargs}
                  :path "/usda/items-nutrients"
                  :on-success [::items-nutrients-res]
                  :on-fail [::items-nutrients-res]}]
      :db db})))

(rf/reg-event-db
 ::items-nutrients-res
 (fn-traced [db [_ eargs]]
            (let [it-nu (:ui.count/items-nutrients db)
                  items (:data eargs)]
              (merge db {:ui.count/items-nutrients-res eargs
                         :ui.count/items-nutrients
                         (merge it-nu
                                (reduce (fn [a item]
                                          (assoc a (:db/id item) item )) {} items))}))))


(rf/reg-event-fx
 ::add-items
 (fn [{:keys [db]} [_ eargs]]
   (let [added (:ui.count/added-items db)]
     {:db (merge db {:ui.count/added-items (vec (concat added eargs))})
      :dispatch [::items-nutrients eargs]})))


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

(rf/reg-event-fx
 ::nhi-dri
 (fn [{:keys [db]} [_ eargs]]
   (let [nhi-dri (:ui.count/nhi-dri-res db)]
     (if nhi-dri
       {:db db}
       {:dispatch [:ui.events/request
                   {:method :get
                    :params {:group "31-50"}
                    :path "/usda/nhi-dri"
                    :on-success [::nhi-dri-res]
                    :on-fail [::nhi-dri-res]}]
        :db db}))))

(rf/reg-event-db
 ::nhi-dri-res
 (fn-traced [db [_ eargs]]
            (assoc db :ui.count/nhi-dri-res eargs)))