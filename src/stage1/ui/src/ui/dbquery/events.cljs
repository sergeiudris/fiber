(ns ui.dbquery.events
  (:require [re-frame.core :as rf]
            [ui.dbquery.spec :as sp]
            [clojure.spec.alpha :as s]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [fiber.spec :as rs]
            [goog.dom]
            [ui.dbquery.core]))




(rf/reg-event-fx
 ::query
 (fn-traced [{:keys [db]} [_ eargs]]
            (let [editor @ui.dbquery.core/editor-query
                  editor-val (if editor (.getValue editor) nil) #_(:ui.dbquery/editor-val db)]
              {:dispatch [:ui.events/request
                          {:method :post
                           :params nil
                           :path "/query"
                           :body {:q-data (cljs.reader/read-string editor-val)}
                           :on-success [::query-resp]
                           :on-fail [::query-resp]}]
               :db db})))

(rf/reg-event-db
 ::query-resp
 (fn-traced [db [_ val]]
            (assoc db :ui.dbquery/query-resp val)))


(rf/reg-event-fx
 ::example-queries
 (fn-traced [{:keys [db]} [_ eargs]]
            (let [data (:ui.dbquery/example-queries-res db)]
              (if data
                {:db db}
                {:dispatch [:ui.events/request
                            {:method :get
                             :params {}
                             :path "/usda/example-queries"
                             :on-success [::example-queries-res]
                             :on-fail [::example-queries-res]}]
                 :db db}))))

(rf/reg-event-db
 ::example-queries-res
 (fn-traced [db [_ val]]
            (assoc db :ui.dbquery/example-queries-res val)))

(rf/reg-event-db
 ::select-query
 (fn [db [_ ea]]
   (let [name (aget ea "name")
         qs (get-in db [:ui.dbquery/example-queries-res :data])
         x (first (filter #(= (:name %) name) qs))
         new-val (:q x)
         new-val-string (with-out-str (cljs.pprint/pprint new-val))]
     (do (.setValue
          (.-session @ui.dbquery.core/editor-query)
          new-val-string))
     db)))

