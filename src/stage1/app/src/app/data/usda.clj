(ns app.data.usda
  (:require [clojure.repl :refer :all]
            [clojure.java.shell :refer [sh] :as shell]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string]))

; (prn "---using .java.shell to list directory:")
; (sh "ls /")

(def data-dir "/opt/data/")

(def DATA_SRC (str "/opt/data/" "sr28asc/DATA_SRC.txt"))

(when-not (.exists (io/file (str data-dir "sr28asc")))
  (sh "bash "))


(comment

  (.exists (io/file DATA_SRC))

  (read-csv-file DATA_SRC)

  (clojure.string/split "3^3^^" #"\^" 100)

  (source clojure.string/split)

  ;
  )



