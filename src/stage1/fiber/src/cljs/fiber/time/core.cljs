(ns fiber.time.core
  (:require [cljs-time.core :as t]))


(defn hi
  []
  "hi")

(defn ts
  []
  (t/epoch)
  5
  )

(comment

  (ts)
  
  (.log js/console (ts))

  ;
  )