(ns ui.dbquery.events
  (:require [re-frame.core :as rf]
            [ui.dbquery.spec :as sp]
            [clojure.spec.alpha :as s]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [fiber.spec :as rs]
            [goog.dom]
            [ui.dbquery.core]))

(rf/reg-event-fx
 ::create
 (fn-traced [{:keys [db]} [_ eargs]]
            (let [editor @ui.dbquery.core/editor
                  editor-val (if editor (.getValue editor) nil) #_(:ui.dbquery/editor-val db)]
              {:dispatch [:ui.events/request
                          {:method :post
                           :params nil
                           :path "/ent"
                           :body {:ent (cljs.reader/read-string editor-val)}
                           :on-success [::create-resp]
                           :on-fail [::create-resp]}]
               :db (assoc db :ui.dbquery/create (.toLocaleTimeString (js/Date.)))})
            ))

(rf/reg-event-db
 ::create-resp
 (fn-traced [db [_ val]]
            (assoc db :ui.dbquery/create-resp val)))




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

(rf/reg-event-db
 ::validate
 (fn-traced [db [_ eargs]]
            (assoc db :ui.dbquery/validate (Math/random))))

(rf/reg-event-db
 ::generate
 (fn [db [_ eargs]]
   (let [new-val (ui.dbquery.spec/gen-resource) #_(rs/gen-ent)
         new-val-string (with-out-str (cljs.pprint/pprint new-val))
         ]
     (.setValue
      (.-session @ui.dbquery.core/editor)
      #_(str (Math/random))
      new-val-string)
     db)))

; #performance!
#_(rf/reg-event-db
 ::editor-val
 (fn-traced [db [_ eargs]]
            (assoc db :ui.dbquery/editor-val eargs)))

#_(s/valid? nil? nil )

#_(s/valid? ::sp/entity {::sp/uuid "ads"
                         ::sp/type :resource})

