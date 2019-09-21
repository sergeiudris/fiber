(ns ui.dbquery.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::create
 (fn [db _]
   (:ui.dbquery/create db)))

(rf/reg-sub
 ::create-resp
 (fn [db _]
   (:ui.dbquery/create-resp db)))

(rf/reg-sub
 ::query-resp
 (fn [db _]
   (:ui.dbquery/query-resp db)))

(rf/reg-sub
 ::validate
 (fn [db _]
   (:ui.dbquery/validate db)))

(rf/reg-sub
 ::generate
 (fn [db _]
   (:ui.dbquery/generate db)))

(rf/reg-sub
 ::editor-val
 (fn [db _]
   (:ui.dbquery/editor-val db)))