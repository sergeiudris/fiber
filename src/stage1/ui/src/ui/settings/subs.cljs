(ns ui.settings.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::settings
 (fn [db _]
   (:ui.settings/settings db)))

