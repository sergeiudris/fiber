(ns ui.monitor.events
  (:require [re-frame.core :as rf]
            [ui.monitor.spec :as sp]
            [clojure.spec.alpha :as s]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [goog.dom]
            [ui.monitor.core]))
