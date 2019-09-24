(ns ui.monitor.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::monitor
 (fn [db _]
   (:ui.monitor/monitor db)))

