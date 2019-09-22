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