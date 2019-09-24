(ns ui.count.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::module-count
 (fn [db _]
   (:ui.core/module-count db)))

(rf/reg-sub
 ::search-res
 (fn [db _]
   (:ui.count/search-res db)))

(rf/reg-sub
 ::results-visible?
 (fn [db _]
   (:ui.count/results-visible? db)))

(rf/reg-sub
 ::search-input
 (fn [db _]
   (:ui.count/search-input db)))

(rf/reg-sub
 ::search-table-mdata
 (fn [db _]
   (:ui.count/search-table-mdata db)))

(rf/reg-sub
 ::selected-items
 (fn [db _]
   (let [added (:ui.count/added-items db)
         items-nutrients (:ui.count/items-nutrients db)]
     (->>
      (map (fn [item]
             (merge item (get items-nutrients (:db/id item)))
             ) added)
      (remove nil?)
      (vec)
      ))))

(rf/reg-sub
 ::nhi-dri-res
 (fn [db _]
   (:ui.count/nhi-dri-res db)))

(rf/reg-sub
 ::nhi-dri
 (fn [db _]
   (-> db (get-in  [:ui.count/nhi-dri-res :data]) (first))))

(rf/reg-sub
 ::nutrients
 (fn [db _]
   (:ui.count/nutrients db)))